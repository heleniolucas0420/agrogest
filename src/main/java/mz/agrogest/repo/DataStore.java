package mz.agrogest.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import mz.agrogest.model.Campanha;
import mz.agrogest.model.Colheita;
import mz.agrogest.model.Cultivo;
import mz.agrogest.model.Fornecedor;
import mz.agrogest.model.Produto;
import mz.agrogest.model.Utilizador;
import mz.agrogest.model.Venda;

/**
 * "Base de dados" em memoria RAM (sem MySQL/PostgreSQL).
 * Singleton: existe uma unica instancia partilhada por todos os pedidos HTTP.
 * As listas sao synchronizedList e os IDs sao gerados por AtomicInteger (RN f).
 */
public final class DataStore {

    private static final DataStore INSTANCIA = new DataStore();

    /** Cadeado unico para operacoes de escrita que mexem em varias listas. */
    public final Object lock = new Object();

    public final List<Utilizador> utilizadores = Collections.synchronizedList(new ArrayList<>());
    public final List<Fornecedor> fornecedores = Collections.synchronizedList(new ArrayList<>());
    public final List<Produto> produtos = Collections.synchronizedList(new ArrayList<>());
    public final List<Campanha> campanhas = Collections.synchronizedList(new ArrayList<>());
    public final List<Cultivo> cultivos = Collections.synchronizedList(new ArrayList<>());
    public final List<Colheita> colheitas = Collections.synchronizedList(new ArrayList<>());
    public final List<Venda> vendas = Collections.synchronizedList(new ArrayList<>());

    public final AtomicInteger seqUtilizador = new AtomicInteger();
    public final AtomicInteger seqFornecedor = new AtomicInteger();
    public final AtomicInteger seqProduto = new AtomicInteger();
    public final AtomicInteger seqCampanha = new AtomicInteger();
    public final AtomicInteger seqCultivo = new AtomicInteger();
    public final AtomicInteger seqColheita = new AtomicInteger();
    public final AtomicInteger seqVenda = new AtomicInteger();
    public final AtomicInteger seqFactura = new AtomicInteger();

    private DataStore() {
    }

    public static DataStore get() {
        return INSTANCIA;
    }

    /** Copia segura de uma lista sincronizada (para iterar sem problemas de concorrencia). */
    public static <T> List<T> copia(List<T> lista) {
        synchronized (lista) {
            return new ArrayList<>(lista);
        }
    }

    /** Cria a fotografia do estado actual para gravar em ficheiro. */
    public Snapshot criarSnapshot() {
        synchronized (lock) {
            Snapshot s = new Snapshot();
            s.utilizadores = new ArrayList<>(copia(utilizadores));
            s.fornecedores = new ArrayList<>(copia(fornecedores));
            s.produtos = new ArrayList<>(copia(produtos));
            s.campanhas = new ArrayList<>(copia(campanhas));
            s.cultivos = new ArrayList<>(copia(cultivos));
            s.colheitas = new ArrayList<>(copia(colheitas));
            s.vendas = new ArrayList<>(copia(vendas));
            s.seqUtilizador = seqUtilizador.get();
            s.seqFornecedor = seqFornecedor.get();
            s.seqProduto = seqProduto.get();
            s.seqCampanha = seqCampanha.get();
            s.seqCultivo = seqCultivo.get();
            s.seqColheita = seqColheita.get();
            s.seqVenda = seqVenda.get();
            s.seqFactura = seqFactura.get();
            return s;
        }
    }

    /** Substitui o estado em memoria pelo que veio do ficheiro. */
    public void restaurar(Snapshot s) {
        synchronized (lock) {
            limpar();
            utilizadores.addAll(s.utilizadores);
            fornecedores.addAll(s.fornecedores);
            produtos.addAll(s.produtos);
            campanhas.addAll(s.campanhas);
            cultivos.addAll(s.cultivos);
            colheitas.addAll(s.colheitas);
            vendas.addAll(s.vendas);
            seqUtilizador.set(s.seqUtilizador);
            seqFornecedor.set(s.seqFornecedor);
            seqProduto.set(s.seqProduto);
            seqCampanha.set(s.seqCampanha);
            seqCultivo.set(s.seqCultivo);
            seqColheita.set(s.seqColheita);
            seqVenda.set(s.seqVenda);
            seqFactura.set(s.seqFactura);
        }
    }

    public void limpar() {
        synchronized (lock) {
            utilizadores.clear();
            fornecedores.clear();
            produtos.clear();
            campanhas.clear();
            cultivos.clear();
            colheitas.clear();
            vendas.clear();
            seqUtilizador.set(0);
            seqFornecedor.set(0);
            seqProduto.set(0);
            seqCampanha.set(0);
            seqCultivo.set(0);
            seqColheita.set(0);
            seqVenda.set(0);
            seqFactura.set(0);
        }
    }
}
