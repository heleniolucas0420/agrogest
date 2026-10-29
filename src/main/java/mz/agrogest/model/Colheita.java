package mz.agrogest.model;

import java.io.Serializable;
import java.time.LocalDate;

import mz.agrogest.util.Formatos;

/** Colheita de um cultivo. O valor total e calculado: kg x preco por kg. */
public class Colheita implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int cultivoId;
    private String cultivoNome;
    private LocalDate data;
    private double quantidadeKg;
    private double precoKg;

    public Colheita() {
    }

    public Colheita(int id, Cultivo cultivo, LocalDate data, double quantidadeKg, double precoKg) {
        this.id = id;
        this.cultivoId = cultivo.getId();
        this.cultivoNome = cultivo.getCultura() + " (" + cultivo.getVariedade() + ")";
        this.data = data;
        this.quantidadeKg = quantidadeKg;
        this.precoKg = precoKg;
    }

    public double getValorTotal() {
        return Formatos.dinheiro(quantidadeKg * precoKg);
    }

    public String getDataTexto() { return Formatos.data(data); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCultivoId() { return cultivoId; }
    public void setCultivoId(int cultivoId) { this.cultivoId = cultivoId; }

    public String getCultivoNome() { return cultivoNome; }
    public void setCultivoNome(String cultivoNome) { this.cultivoNome = cultivoNome; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public double getQuantidadeKg() { return quantidadeKg; }
    public void setQuantidadeKg(double quantidadeKg) { this.quantidadeKg = quantidadeKg; }

    public double getPrecoKg() { return precoKg; }
    public void setPrecoKg(double precoKg) { this.precoKg = precoKg; }
}
