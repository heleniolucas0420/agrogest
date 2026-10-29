package mz.agrogest.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mz.agrogest.model.ResumoCliente;
import mz.agrogest.model.Utilizador;
import mz.agrogest.model.Venda;
import mz.agrogest.repo.DataStore;
import mz.agrogest.util.Formatos;

/** Clientes e relatórios sobre clientes/vendas. */
public class ClienteService {

    private final DataStore ds = DataStore.get();

    public List<Utilizador> listarClientes(String ordem, boolean desc) {
        List<Utilizador> lista = new ArrayList<>();
        for (Utilizador u : DataStore.copia(ds.utilizadores)) {
            if (!u.isAdmin()) {
                lista.add(u);
            }
        }
        Comparator<Utilizador> c;
        if ("data".equals(ordem)) {
            c = Comparator.comparing(Utilizador::getDataRegisto,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        } else if ("email".equals(ordem)) {
            c = Comparator.comparing(Utilizador::getEmail);
        } else {
            c = Comparator.comparing(u -> u.getNome().toLowerCase());
        }
        lista.sort(desc ? c.reversed() : c);
        return lista;
    }

    /** Activar / desactivar uma conta de cliente (conta inactiva não faz login). */
    public void alternarAtivo(int id) {
        synchronized (ds.lock) {
            for (Utilizador u : DataStore.copia(ds.utilizadores)) {
                if (u.getId() == id) {
                    if (u.isAdmin()) {
                        throw new ValidacaoException("A conta do administrador não pode ser desactivada.");
                    }
                    u.setAtivo(!u.isAtivo());
                    return;
                }
            }
            throw new ValidacaoException("Cliente não encontrado.");
        }
    }

    /** Junta cada cliente com o nº de compras e o total gasto. */
    private List<ResumoCliente> resumos() {
        Map<Integer, ResumoCliente> mapa = new LinkedHashMap<>();
        for (Utilizador u : listarClientes("nome", false)) {
            mapa.put(u.getId(), new ResumoCliente(u));
        }
        for (Venda v : DataStore.copia(ds.vendas)) {
            ResumoCliente r = mapa.get(v.getClienteId());
            if (r != null) {
                r.setNumeroCompras(r.getNumeroCompras() + 1);
                r.setTotalGasto(Formatos.dinheiro(r.getTotalGasto() + v.getTotal()));
            }
        }
        return new ArrayList<>(mapa.values());
    }

    /** Relatório 1: clientes ordenados por nome (crescente). */
    public List<ResumoCliente> clientesPorNome() {
        return resumos(); // já vem ordenado por nome
    }

    /** Relatório 2: clientes que compraram, ordenados por volume (valor gasto, decrescente). */
    public List<ResumoCliente> clientesPorVolume() {
        List<ResumoCliente> lista = new ArrayList<>();
        for (ResumoCliente r : resumos()) {
            if (r.getNumeroCompras() > 0) {
                lista.add(r);
            }
        }
        lista.sort(Comparator.comparingDouble(ResumoCliente::getTotalGasto).reversed());
        return lista;
    }

    /** Relatório 3: clientes mais assíduos (ranking por número de compras). */
    public List<ResumoCliente> clientesMaisAssiduos() {
        List<ResumoCliente> lista = new ArrayList<>();
        for (ResumoCliente r : resumos()) {
            if (r.getNumeroCompras() > 0) {
                lista.add(r);
            }
        }
        lista.sort(Comparator.comparingInt(ResumoCliente::getNumeroCompras).reversed()
                .thenComparing(Comparator.comparingDouble(ResumoCliente::getTotalGasto).reversed()));
        return lista;
    }

    /** Relatório extra: clientes sem compras. */
    public List<ResumoCliente> clientesSemCompras() {
        List<ResumoCliente> lista = new ArrayList<>();
        for (ResumoCliente r : resumos()) {
            if (r.getNumeroCompras() == 0) {
                lista.add(r);
            }
        }
        return lista;
    }
}
