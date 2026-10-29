package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.model.Utilizador;
import mz.agrogest.model.Venda;
import mz.agrogest.service.VendaService;

/**
 * Mostra uma factura. RN c: o cliente só vê as suas; o admin vê todas.
 * Com ?imprimir=1 mostra a versão limpa para impressão.
 */
@WebServlet("/factura")
public class FacturaServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final VendaService vendas = new VendaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilizador u = utilizador(req);
        Venda v = vendas.buscar(inteiro(req, "id", -1));
        if (v == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Factura não encontrada.");
            return;
        }
        if (!u.isAdmin() && v.getClienteId() != u.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Esta factura não lhe pertence.");
            return;
        }
        req.setAttribute("venda", v);
        req.setAttribute("taxaIva", 16);
        if ("1".equals(req.getParameter("imprimir"))) {
            req.setAttribute("titulo", "Factura " + v.getNumeroFactura());
            req.getRequestDispatcher("/WEB-INF/views/factura-impressao.jsp").forward(req, resp);
        } else {
            vista(req, resp, "factura.jsp", "Factura " + v.getNumeroFactura());
        }
    }
}
