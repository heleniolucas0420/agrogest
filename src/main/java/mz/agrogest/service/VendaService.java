package mz.agrogest.service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.agrogest.model.Carrinho;
import mz.agrogest.model.ItemCarrinho;
import mz.agrogest.model.Produto;
import mz.agrogest.model.Utilizador;
import mz.agrogest.model.Venda;
import mz.agrogest.model.VendaItem;
import mz.agrogest.repo.DataStore;
import mz.agrogest.util.Formatos;

/**
 * Vendas: a parte mais importante das regras de negócio.
 *
 * finalizarCompra() é uma "transacção lógica": corre dentro de synchronized,
 * por isso dois clientes não conseguem comprar o mesmo stock ao mesmo tempo.
 * Funciona em 2 fases (tudo ou nada):
 *   1) verifica TODOS os itens (existe? há stock suficiente?);
 *   2) só se tudo estiver bem, desconta o stock de todos e cria a factura.
 */
public class VendaService {

    private final DataStore ds = DataStore.get();
    private final ProdutoService produtos = new ProdutoService();

    public Venda finalizarCompra(Utilizador cliente, Carrinho carrinho) {
        if (cliente == null || cliente.isAdmin()) {
            throw new ValidacaoException("Apenas clientes podem efectuar compras.");
        }
        if (carrinho == null || carrinho.isVazio()) {
            throw new ValidacaoException("O carrinho está vazio.");
        }

        synchronized (ds.lock) {
            List<ItemCarrinho> itens = carrinho.getItens();

            // ---- Fase 1: validar tudo (RN a: não vender mais do que o stock) ----
            List<String> problemas = new ArrayList<>();
            List<Produto> encontrados = new ArrayList<>();
            for (ItemCarrinho item : itens) {
                Produto p = produtos.buscar(item.getProdutoId());
                if (p == null) {
                    problemas.add("\"" + item.getNome() + "\" já não existe no catálogo");
                } else if (p.isExpirado()) {
                    problemas.add("\"" + p.getNome() + "\" está fora da validade (" + p.getValidadeTexto() + ")");
                } else if (item.getQuantidade() <= 0) {
                    problemas.add("quantidade inválida para \"" + p.getNome() + "\"");
                } else if (p.getQuantidade() < item.getQuantidade()) {
                    problemas.add("\"" + p.getNome() + "\": pediu " + item.getQuantidade() + " "
                            + p.getUnidade() + ", disponível " + p.getQuantidade());
                }
                encontrados.add(p);
            }
            if (!problemas.isEmpty()) {
                throw new ValidacaoException("Produto indisponível — " + String.join("; ", problemas)
                        + ". Nada foi cobrado.");
            }

            // ---- Fase 2: descontar stock de todos os itens (RN b: atómico) ----
            Venda v = new Venda();
            double subtotal = 0;
            for (int i = 0; i < itens.size(); i++) {
                ItemCarrinho item = itens.get(i);
                Produto p = encontrados.get(i);
                p.setQuantidade(p.getQuantidade() - item.getQuantidade());
                VendaItem vi = new VendaItem(p.getId(), p.getNome(), p.getUnidade(),
                        item.getQuantidade(), p.getPreco());
                v.getItens().add(vi);
                subtotal += vi.getSubtotal();
            }

            subtotal = Formatos.dinheiro(subtotal);
            double iva = Formatos.dinheiro(subtotal * Carrinho.TAXA_IVA);
            v.setId(ds.seqVenda.incrementAndGet());
            v.setNumeroFactura("FT-" + Year.now().getValue() + "-" + ds.seqFactura.incrementAndGet());
            v.setClienteId(cliente.getId());
            v.setClienteNome(cliente.getNome());
            v.setClienteEmail(cliente.getEmail());
            v.setClienteTelefone(cliente.getTelefone());
            v.setClienteEndereco(cliente.getEndereco());
            v.setData(LocalDateTime.now());
            v.setSubtotal(subtotal);
            v.setIva(iva);
            v.setTotal(Formatos.dinheiro(subtotal + iva));
            ds.vendas.add(v);

            carrinho.esvaziar();
            return v;
        }
    }

    /** Verifica se é possível ter "quantidadeTotal" deste produto no carrinho. */
    public void verificarDisponibilidade(int produtoId, int quantidadeTotal) {
        Produto p = produtos.buscar(produtoId);
        if (p == null) {
            throw new ValidacaoException("Produto não encontrado.");
        }
        if (p.isExpirado()) {
            throw new ValidacaoException("\"" + p.getNome() + "\" está fora da validade ("
                    + p.getValidadeTexto() + ") e não pode ser vendido.");
        }
        if (quantidadeTotal <= 0) {
            throw new ValidacaoException("A quantidade deve ser maior que zero.");
        }
        if (quantidadeTotal > p.getQuantidade()) {
            throw new ValidacaoException("Stock insuficiente para \"" + p.getNome() + "\": disponível "
                    + p.getQuantidade() + " " + p.getUnidade() + ".");
        }
    }

    public List<Venda> listarTodas(String ordem, boolean desc) {
        List<Venda> lista = DataStore.copia(ds.vendas);
        Comparator<Venda> c;
        if ("valor".equals(ordem)) {
            c = Comparator.comparingDouble(Venda::getTotal);
        } else if ("cliente".equals(ordem)) {
            c = Comparator.comparing(v -> v.getClienteNome().toLowerCase());
        } else {
            c = Comparator.comparing(Venda::getData);
        }
        lista.sort(desc ? c.reversed() : c);
        return lista;
    }

    /** RN c: o cliente só vê as suas facturas. */
    public List<Venda> listarDoCliente(int clienteId) {
        List<Venda> lista = new ArrayList<>();
        for (Venda v : DataStore.copia(ds.vendas)) {
            if (v.getClienteId() == clienteId) {
                lista.add(v);
            }
        }
        lista.sort(Comparator.comparing(Venda::getData).reversed());
        return lista;
    }

    public Venda buscar(int id) {
        for (Venda v : DataStore.copia(ds.vendas)) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public double totalFacturado() {
        double soma = 0;
        for (Venda v : DataStore.copia(ds.vendas)) {
            soma += v.getTotal();
        }
        return Formatos.dinheiro(soma);
    }
}
