package mz.agrogest.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.agrogest.model.Categoria;
import mz.agrogest.model.Fornecedor;
import mz.agrogest.model.Produto;
import mz.agrogest.repo.DataStore;
import mz.agrogest.util.Validar;

/** Regras de negócio dos produtos (catálogo e stock). */
public class ProdutoService {

    private final DataStore ds = DataStore.get();
    private final FornecedorService fornecedores = new FornecedorService();

    public List<Produto> listar(String ordem, boolean desc) {
        List<Produto> lista = DataStore.copia(ds.produtos);
        ordenar(lista, ordem, desc);
        return lista;
    }

    /** Produtos em armazém = stock maior que zero. */
    public List<Produto> emArmazem(String ordem, boolean desc) {
        List<Produto> lista = new ArrayList<>();
        for (Produto p : DataStore.copia(ds.produtos)) {
            if (p.getQuantidade() > 0) {
                lista.add(p);
            }
        }
        ordenar(lista, ordem, desc);
        return lista;
    }

    /** Catálogo do cliente, com filtro opcional por categoria. */
    public List<Produto> catalogo(Categoria categoria, String ordem) {
        List<Produto> lista = new ArrayList<>();
        for (Produto p : DataStore.copia(ds.produtos)) {
            if (categoria == null || p.getCategoria() == categoria) {
                lista.add(p);
            }
        }
        ordenar(lista, ordem, false);
        return lista;
    }

    private void ordenar(List<Produto> lista, String ordem, boolean desc) {
        Comparator<Produto> c;
        if ("preco".equals(ordem)) {
            c = Comparator.comparingDouble(Produto::getPreco);
        } else if ("stock".equals(ordem)) {
            c = Comparator.comparingInt(Produto::getQuantidade);
        } else if ("validade".equals(ordem) || "data".equals(ordem)) {
            c = Comparator.comparing(Produto::getValidade, Comparator.nullsLast(Comparator.naturalOrder()));
        } else if ("categoria".equals(ordem)) {
            c = Comparator.comparing(p -> p.getCategoria().name());
        } else if ("id".equals(ordem)) {
            c = Comparator.comparingInt(Produto::getId);
        } else {
            c = Comparator.comparing(p -> p.getNome().toLowerCase());
        }
        lista.sort(desc ? c.reversed() : c);
    }

    public Produto criar(String nome, String categoria, String fornecedorId, String preco, String quantidade,
                         String unidade, String descricao, String foto, String validade) {
        String n = Validar.texto(nome, "Nome");
        Categoria cat = Validar.enumeracao(Categoria.class, categoria, "Categoria");
        Fornecedor f = fornecedores.buscar(Validar.inteiro(fornecedorId, "Fornecedor"));
        if (f == null) {
            throw new ValidacaoException("Escolha um fornecedor válido.");
        }
        double pr = Validar.decimalPositivo(preco, "Preço");
        int q = Validar.inteiro(quantidade, "Quantidade");
        if (q < 0) {
            throw new ValidacaoException("A quantidade não pode ser negativa.");
        }
        String u = Validar.texto(unidade, "Unidade");
        String d = Validar.opcional(descricao);
        String fo = Validar.opcional(foto);
        LocalDate v = Validar.data(validade, "Validade");
        if (v.isBefore(LocalDate.now())) {
            throw new ValidacaoException("A validade não pode ser uma data que já passou.");
        }

        synchronized (ds.lock) {
            Produto p = new Produto(ds.seqProduto.incrementAndGet(), n, cat, f, pr, q, u, d, fo, v);
            ds.produtos.add(p);
            return p;
        }
    }

    /** Repor stock (soma quantidade) e, opcionalmente, alterar o preço. */
    public Produto atualizar(int id, String reporQuantidade, String novoPreco) {
        synchronized (ds.lock) {
            Produto p = buscar(id);
            if (p == null) {
                throw new ValidacaoException("Produto não encontrado.");
            }
            String rq = Validar.opcional(reporQuantidade);
            String np = Validar.opcional(novoPreco);
            if (rq.isEmpty() && np.isEmpty()) {
                throw new ValidacaoException("Indique a quantidade a repor ou o novo preço.");
            }
            if (!rq.isEmpty()) {
                int q = Validar.inteiroPositivo(rq, "Quantidade a repor");
                p.setQuantidade(p.getQuantidade() + q);
            }
            if (!np.isEmpty()) {
                p.setPreco(Validar.decimalPositivo(np, "Novo preço"));
            }
            return p;
        }
    }

    public void remover(int id) {
        synchronized (ds.lock) {
            Produto p = buscar(id);
            if (p == null) {
                throw new ValidacaoException("Produto não encontrado.");
            }
            ds.produtos.remove(p);
        }
    }

    public Produto buscar(int id) {
        for (Produto p : DataStore.copia(ds.produtos)) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
