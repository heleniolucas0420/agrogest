package mz.agrogest.service;

import java.util.Comparator;
import java.util.List;

import mz.agrogest.model.Fornecedor;
import mz.agrogest.model.Produto;
import mz.agrogest.model.TipoFornecedor;
import mz.agrogest.repo.DataStore;
import mz.agrogest.util.Validar;

/** Regras de negócio dos fornecedores. */
public class FornecedorService {

    private final DataStore ds = DataStore.get();

    public List<Fornecedor> listar(String ordem, boolean desc) {
        List<Fornecedor> lista = DataStore.copia(ds.fornecedores);
        Comparator<Fornecedor> c;
        if ("tipo".equals(ordem)) {
            c = Comparator.comparing(f -> f.getTipo().name());
        } else if ("provincia".equals(ordem)) {
            c = Comparator.comparing(f -> f.getProvincia().toLowerCase());
        } else if ("id".equals(ordem)) {
            c = Comparator.comparingInt(Fornecedor::getId);
        } else {
            c = Comparator.comparing(f -> f.getNome().toLowerCase());
        }
        lista.sort(desc ? c.reversed() : c);
        return lista;
    }

    public Fornecedor criar(String nome, String tipo, String contacto, String provincia) {
        String n = Validar.texto(nome, "Nome");
        TipoFornecedor t = Validar.enumeracao(TipoFornecedor.class, tipo, "Tipo");
        String c = Validar.texto(contacto, "Contacto");
        String p = Validar.texto(provincia, "Província");
        synchronized (ds.lock) {
            for (Fornecedor f : DataStore.copia(ds.fornecedores)) {
                if (f.getNome().equalsIgnoreCase(n)) {
                    throw new ValidacaoException("Já existe um fornecedor com o nome \"" + n + "\".");
                }
            }
            Fornecedor f = new Fornecedor(ds.seqFornecedor.incrementAndGet(), n, t, c, p);
            ds.fornecedores.add(f);
            return f;
        }
    }

    public void remover(int id) {
        synchronized (ds.lock) {
            Fornecedor f = buscar(id);
            if (f == null) {
                throw new ValidacaoException("Fornecedor não encontrado.");
            }
            for (Produto p : DataStore.copia(ds.produtos)) {
                if (p.getFornecedorId() == id) {
                    throw new ValidacaoException("Não é possível remover \"" + f.getNome()
                            + "\": tem produtos associados (ex.: " + p.getNome() + ").");
                }
            }
            ds.fornecedores.remove(f);
        }
    }

    public Fornecedor buscar(int id) {
        for (Fornecedor f : DataStore.copia(ds.fornecedores)) {
            if (f.getId() == id) {
                return f;
            }
        }
        return null;
    }
}
