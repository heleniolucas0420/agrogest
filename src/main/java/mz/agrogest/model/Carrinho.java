package mz.agrogest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mz.agrogest.util.Formatos;

/** Carrinho de compras guardado na sessao do cliente. Chave = id do produto. */
public class Carrinho implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final double TAXA_IVA = 0.16;

    private final Map<Integer, ItemCarrinho> itens = new LinkedHashMap<>();

    public synchronized void adicionar(Produto p, int quantidade) {
        ItemCarrinho item = itens.get(p.getId());
        if (item == null) {
            itens.put(p.getId(), new ItemCarrinho(p, quantidade));
        } else {
            item.setQuantidade(item.getQuantidade() + quantidade);
            item.setPreco(p.getPreco()); // preco mais recente
        }
    }

    public synchronized void atualizar(int produtoId, int quantidade) {
        ItemCarrinho item = itens.get(produtoId);
        if (item == null) {
            return;
        }
        if (quantidade <= 0) {
            itens.remove(produtoId);
        } else {
            item.setQuantidade(quantidade);
        }
    }

    public synchronized void remover(int produtoId) {
        itens.remove(produtoId);
    }

    public synchronized void esvaziar() {
        itens.clear();
    }

    public synchronized int quantidadeDe(int produtoId) {
        ItemCarrinho item = itens.get(produtoId);
        return item == null ? 0 : item.getQuantidade();
    }

    public synchronized List<ItemCarrinho> getItens() {
        return new ArrayList<>(itens.values());
    }

    public synchronized boolean isVazio() {
        return itens.isEmpty();
    }

    public synchronized int getTotalItens() {
        int soma = 0;
        for (ItemCarrinho i : itens.values()) {
            soma += i.getQuantidade();
        }
        return soma;
    }

    public synchronized double getSubtotal() {
        double soma = 0;
        for (ItemCarrinho i : itens.values()) {
            soma += i.getSubtotal();
        }
        return Formatos.dinheiro(soma);
    }

    public double getIva() {
        return Formatos.dinheiro(getSubtotal() * TAXA_IVA);
    }

    public double getTotal() {
        return Formatos.dinheiro(getSubtotal() + getIva());
    }
}
