package mz.agrogest.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import mz.agrogest.util.Formatos;

/**
 * Venda / factura. Guarda uma copia dos dados do cliente no momento da compra,
 * para a factura nao mudar se o cliente alterar dados depois.
 */
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String numeroFactura;
    private int clienteId;
    private String clienteNome;
    private String clienteEmail;
    private String clienteTelefone;
    private String clienteEndereco;
    private LocalDateTime data;
    private List<VendaItem> itens = new ArrayList<>();
    private double subtotal;
    private double iva;
    private double total;

    public Venda() {
    }

    public String getDataTexto() {
        return Formatos.dataHora(data);
    }

    public int getTotalUnidades() {
        int soma = 0;
        for (VendaItem i : itens) {
            soma += i.getQuantidade();
        }
        return soma;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getClienteTelefone() { return clienteTelefone; }
    public void setClienteTelefone(String clienteTelefone) { this.clienteTelefone = clienteTelefone; }

    public String getClienteEndereco() { return clienteEndereco; }
    public void setClienteEndereco(String clienteEndereco) { this.clienteEndereco = clienteEndereco; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public List<VendaItem> getItens() { return itens; }
    public void setItens(List<VendaItem> itens) { this.itens = itens; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
