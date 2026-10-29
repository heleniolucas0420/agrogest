package mz.agrogest.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.service.ClienteService;
import mz.agrogest.service.FornecedorService;
import mz.agrogest.service.ProdutoService;
import mz.agrogest.service.ValidacaoException;
import mz.agrogest.servlet.BaseServlet;

/**
 * Listagens com ordenação: produtos, fornecedores, clientes, armazém (stock > 0).
 * As compras efectuadas estão em /admin/compras.
 */
@WebServlet("/admin/listagens")
public class ListagensServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final ProdutoService produtos = new ProdutoService();
    private final FornecedorService fornecedores = new FornecedorService();
    private final ClienteService clientes = new ClienteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tipo = req.getParameter("tipo");
        if (tipo == null) {
            tipo = "produtos";
        }
        String ordem = req.getParameter("ordem");
        boolean desc = desc(req);
        switch (tipo) {
            case "fornecedores":
                req.setAttribute("lista", fornecedores.listar(ordem, desc));
                break;
            case "clientes":
                req.setAttribute("lista", clientes.listarClientes(ordem, desc));
                break;
            case "armazem":
                req.setAttribute("lista", produtos.emArmazem(ordem, desc));
                break;
            default:
                tipo = "produtos";
                req.setAttribute("lista", produtos.listar(ordem, desc));
        }
        req.setAttribute("tipo", tipo);
        req.setAttribute("ordem", ordem == null ? "nome" : ordem);
        req.setAttribute("dir", desc ? "desc" : "asc");
        vista(req, resp, "admin/listagens.jsp", "Listagens");
    }

    /** Activar / desactivar cliente a partir da listagem de clientes. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("alternarCliente".equals(req.getParameter("acao"))) {
            try {
                clientes.alternarAtivo(inteiro(req, "id", -1));
                sucesso(req, "Estado do cliente actualizado.");
            } catch (ValidacaoException e) {
                erro(req, e.getMessage());
            }
        }
        Persistencia.gravarSilencioso();
        redirecionar(req, resp, "/admin/listagens?tipo=clientes");
    }
}
