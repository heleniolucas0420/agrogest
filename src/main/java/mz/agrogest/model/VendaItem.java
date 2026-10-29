package mz.agrogest.model;

import java.io.Serializable;

import mz.agrogest.util.Formatos;

/** Linha de uma venda (produto, quantidade, preco unitario). */
public class VendaItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int produtoId;
    private String produtoNome;
    private String unidade;
    private int quantidade;
    private double precoUnit;

    public VendaItem() {
    }

    public VendaItem(int produtoId, String produtoNome, String unidade, int quantidade, double precoUnit) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.precoUnit = precoUnit;
    }

    public double getSubtotal() {
        return Formatos.dinheiro(quantidade * precoUnit);
    }

    public int getProdutoId() { return produtoId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPrecoUnit() { return precoUnit; }
    public void setPrecoUnit(double precoUnit) { this.precoUnit = precoUnit; }
}
