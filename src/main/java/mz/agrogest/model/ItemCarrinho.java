package mz.agrogest.model;

import java.io.Serializable;

import mz.agrogest.util.Formatos;

/** Linha do carrinho de compras (fica na sessao HTTP). */
public class ItemCarrinho implements Serializable {

    private static final long serialVersionUID = 1L;

    private int produtoId;
    private String nome;
    private String unidade;
    private String foto;
    private boolean fotoExterna;
    private double preco;
    private int quantidade;

    public ItemCarrinho() {
    }

    public ItemCarrinho(Produto p, int quantidade) {
        this.produtoId = p.getId();
        this.nome = p.getNome();
        this.unidade = p.getUnidade();
        this.foto = p.getFoto();
        this.fotoExterna = p.isFotoExterna();
        this.preco = p.getPreco();
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return Formatos.dinheiro(preco * quantidade);
    }

    public int getProdutoId() { return produtoId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public boolean isFotoExterna() { return fotoExterna; }
    public void setFotoExterna(boolean fotoExterna) { this.fotoExterna = fotoExterna; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}
