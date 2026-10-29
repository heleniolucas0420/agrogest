package mz.agrogest.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.service.ClienteService;
import mz.agrogest.servlet.BaseServlet;

/** Relatórios de clientes e vendas. */
@WebServlet("/admin/relatorios")
public class RelatoriosServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final ClienteService clientes = new ClienteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("porNome", clientes.clientesPorNome());
        req.setAttribute("porVolume", clientes.clientesPorVolume());
        req.setAttribute("assiduos", clientes.clientesMaisAssiduos());
        req.setAttribute("semCompras", clientes.clientesSemCompras());
        vista(req, resp, "admin/relatorios.jsp", "Relatórios");
    }
}
