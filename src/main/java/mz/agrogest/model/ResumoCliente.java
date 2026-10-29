package mz.agrogest.model;

import java.io.Serializable;

/** Linha dos relatórios: um cliente com o número de compras e o valor gasto. */
public class ResumoCliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Utilizador cliente;
    private int numeroCompras;
    private double totalGasto;

    public ResumoCliente() {
    }

    public ResumoCliente(Utilizador cliente) {
        this.cliente = cliente;
    }

    public Utilizador getCliente() { return cliente; }
    public void setCliente(Utilizador cliente) { this.cliente = cliente; }

    public int getNumeroCompras() { return numeroCompras; }
    public void setNumeroCompras(int numeroCompras) { this.numeroCompras = numeroCompras; }

    public double getTotalGasto() { return totalGasto; }
    public void setTotalGasto(double totalGasto) { this.totalGasto = totalGasto; }
}
