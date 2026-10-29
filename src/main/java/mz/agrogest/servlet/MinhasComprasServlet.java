package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.model.Utilizador;
import mz.agrogest.service.VendaService;

/** Histórico de compras do cliente autenticado. */
@WebServlet("/minhas-compras")
public class MinhasComprasServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final VendaService vendas = new VendaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilizador u = utilizador(req);
        if (u.isAdmin()) {
            redirecionar(req, resp, "/admin/compras");
            return;
        }
        req.setAttribute("vendas", vendas.listarDoCliente(u.getId()));
        vista(req, resp, "cliente/minhas-compras.jsp", "Minhas compras");
    }
}
