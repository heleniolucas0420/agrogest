package mz.agrogest.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.model.Fornecedor;
import mz.agrogest.model.TipoFornecedor;
import mz.agrogest.service.FornecedorService;
import mz.agrogest.service.ValidacaoException;
import mz.agrogest.servlet.BaseServlet;

/** Gestão de fornecedores: criar, listar, remover. */
@WebServlet("/admin/fornecedores")
public class FornecedoresServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final FornecedorService service = new FornecedorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("fornecedores", service.listar(req.getParameter("ordem"), desc(req)));
        req.setAttribute("tipos", TipoFornecedor.values());
        vista(req, resp, "admin/fornecedores.jsp", "Fornecedores");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        try {
            if ("criar".equals(acao)) {
                Fornecedor f = service.criar(req.getParameter("nome"), req.getParameter("tipo"),
                        req.getParameter("contacto"), req.getParameter("provincia"));
                sucesso(req, "Fornecedor \"" + f.getNome() + "\" criado.");
            } else if ("remover".equals(acao)) {
                service.remover(inteiro(req, "id", -1));
                sucesso(req, "Fornecedor removido.");
            }
        } catch (ValidacaoException e) {
            erro(req, e.getMessage());
        }
        Persistencia.gravarSilencioso();
        redirecionar(req, resp, "/admin/fornecedores");
    }
}
