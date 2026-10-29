package mz.agrogest.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.agrogest.model.Campanha;
import mz.agrogest.model.Colheita;
import mz.agrogest.model.Cultivo;
import mz.agrogest.repo.DataStore;
import mz.agrogest.util.Validar;

/** Campanhas agrícolas, cultivos e colheitas. */
public class CampanhaService {

    private final DataStore ds = DataStore.get();

    // ---------------- Campanhas ----------------

    public List<Campanha> listarCampanhas() {
        List<Campanha> lista = DataStore.copia(ds.campanhas);
        lista.sort(Comparator.comparing(Campanha::getInicio).reversed());
        return lista;
    }

    public Campanha criarCampanha(String nome, String inicio, String fim) {
        String n = Validar.texto(nome, "Nome");
        LocalDate i = Validar.data(inicio, "Data de início");
        LocalDate f = Validar.data(fim, "Data de fim");
        if (f.isBefore(i)) {
            throw new ValidacaoException("A data de fim não pode ser anterior à data de início.");
        }
        synchronized (ds.lock) {
            Campanha c = new Campanha(ds.seqCampanha.incrementAndGet(), n, i, f);
            ds.campanhas.add(c);
            return c;
        }
    }

    public void alterarEstado(int id, boolean aberta) {
        synchronized (ds.lock) {
            Campanha c = buscarCampanha(id);
            if (c == null) {
                throw new ValidacaoException("Campanha não encontrada.");
            }
            c.setAberta(aberta);
        }
    }

    public void removerCampanha(int id) {
        synchronized (ds.lock) {
            Campanha c = buscarCampanha(id);
            if (c == null) {
                throw new ValidacaoException("Campanha não encontrada.");
            }
            if (!listarCultivos(id).isEmpty()) {
                throw new ValidacaoException("Não é possível remover \"" + c.getNome()
                        + "\": tem cultivos associados.");
            }
            ds.campanhas.remove(c);
        }
    }

    public Campanha buscarCampanha(int id) {
        for (Campanha c : DataStore.copia(ds.campanhas)) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public long totalCultivos(int campanhaId) {
        return listarCultivos(campanhaId).size();
    }

    // ---------------- Cultivos ----------------

    /** @param campanhaId 0 = todas as campanhas */
    public List<Cultivo> listarCultivos(int campanhaId) {
        List<Cultivo> lista = new ArrayList<>();
        for (Cultivo c : DataStore.copia(ds.cultivos)) {
            if (campanhaId == 0 || c.getCampanhaId() == campanhaId) {
                lista.add(c);
            }
        }
        lista.sort(Comparator.comparing(Cultivo::getDataPlantio).reversed());
        return lista;
    }

    public Cultivo criarCultivo(String campanhaId, String cultura, String variedade, String hectares,
                                String dataPlantio) {
        Campanha camp = buscarCampanha(Validar.inteiro(campanhaId, "Campanha"));
        if (camp == null) {
            throw new ValidacaoException("Escolha uma campanha válida.");
        }
        if (!camp.isAberta()) {
            throw new ValidacaoException("A campanha \"" + camp.getNome() + "\" está fechada. Reabra-a primeiro.");
        }
        String cu = Validar.texto(cultura, "Cultura");
        String va = Validar.texto(variedade, "Variedade");
        double ha = Validar.decimalPositivo(hectares, "Hectares");
        LocalDate dp = Validar.data(dataPlantio, "Data de plantio");
        synchronized (ds.lock) {
            Cultivo c = new Cultivo(ds.seqCultivo.incrementAndGet(), camp, cu, va, ha, dp);
            ds.cultivos.add(c);
            return c;
        }
    }

    public void removerCultivo(int id) {
        synchronized (ds.lock) {
            Cultivo c = buscarCultivo(id);
            if (c == null) {
                throw new ValidacaoException("Cultivo não encontrado.");
            }
            if (!listarColheitas(id).isEmpty()) {
                throw new ValidacaoException("Não é possível remover o cultivo \"" + c.getCultura()
                        + "\": tem colheitas associadas.");
            }
            ds.cultivos.remove(c);
        }
    }

    public Cultivo buscarCultivo(int id) {
        for (Cultivo c : DataStore.copia(ds.cultivos)) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    // ---------------- Colheitas ----------------

    /** @param cultivoId 0 = todos os cultivos */
    public List<Colheita> listarColheitas(int cultivoId) {
        List<Colheita> lista = new ArrayList<>();
        for (Colheita c : DataStore.copia(ds.colheitas)) {
            if (cultivoId == 0 || c.getCultivoId() == cultivoId) {
                lista.add(c);
            }
        }
        lista.sort(Comparator.comparing(Colheita::getData).reversed());
        return lista;
    }

    public Colheita criarColheita(String cultivoId, String data, String quantidadeKg, String precoKg) {
        Cultivo cu = buscarCultivo(Validar.inteiro(cultivoId, "Cultivo"));
        if (cu == null) {
            throw new ValidacaoException("Escolha um cultivo válido.");
        }
        LocalDate d = Validar.data(data, "Data");
        if (cu.getDataPlantio() != null && d.isBefore(cu.getDataPlantio())) {
            throw new ValidacaoException("A colheita não pode ser antes da data de plantio ("
                    + cu.getDataPlantioTexto() + ").");
        }
        double kg = Validar.decimalPositivo(quantidadeKg, "Quantidade (kg)");
        double pk = Validar.decimalPositivo(precoKg, "Preço por kg");
        synchronized (ds.lock) {
            Colheita c = new Colheita(ds.seqColheita.incrementAndGet(), cu, d, kg, pk);
            ds.colheitas.add(c);
            return c;
        }
    }

    public void removerColheita(int id) {
        synchronized (ds.lock) {
            Colheita alvo = null;
            for (Colheita c : DataStore.copia(ds.colheitas)) {
                if (c.getId() == id) {
                    alvo = c;
                }
            }
            if (alvo == null) {
                throw new ValidacaoException("Colheita não encontrada.");
            }
            ds.colheitas.remove(alvo);
        }
    }

    public double valorTotalColheitas() {
        double soma = 0;
        for (Colheita c : DataStore.copia(ds.colheitas)) {
            soma += c.getValorTotal();
        }
        return soma;
    }
}
