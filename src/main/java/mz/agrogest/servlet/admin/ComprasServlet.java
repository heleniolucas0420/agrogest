package mz.agrogest.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.service.VendaService;
import mz.agrogest.servlet.BaseServlet;

/** Compras efectuadas (detalhadas), com ordenação por data, valor ou cliente. */
@WebServlet("/admin/compras")
public class ComprasServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final VendaService vendas = new VendaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ordem = req.getParameter("ordem");
        boolean desc = req.getParameter("dir") == null || desc(req); // por defeito: mais recentes primeiro
        req.setAttribute("vendas", vendas.listarTodas(ordem, desc));
        req.setAttribute("ordem", ordem == null ? "data" : ordem);
        req.setAttribute("dir", desc ? "desc" : "asc");
        req.setAttribute("totalFacturado", vendas.totalFacturado());
        vista(req, resp, "admin/compras.jsp", "Compras detalhadas");
    }
}
