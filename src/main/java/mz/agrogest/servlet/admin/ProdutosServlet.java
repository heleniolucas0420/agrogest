package mz.agrogest.servlet.admin;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.model.Categoria;
import mz.agrogest.model.Produto;
import mz.agrogest.service.FornecedorService;
import mz.agrogest.service.ProdutoService;
import mz.agrogest.service.ValidacaoException;
import mz.agrogest.servlet.BaseServlet;

/** Gestão de produtos: criar, listar, actualizar (repor stock / preço), remover. */
@WebServlet("/admin/produtos")
public class ProdutosServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final ProdutoService service = new ProdutoService();
    private final FornecedorService fornecedores = new FornecedorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("produtos", service.listar(req.getParameter("ordem"), desc(req)));
        req.setAttribute("fornecedores", fornecedores.listar("nome", false));
        req.setAttribute("categorias", Categoria.values());
        req.setAttribute("validadePadrao", LocalDate.now().plusMonths(8).toString());
        vista(req, resp, "admin/produtos.jsp", "Produtos");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        try {
            if ("criar".equals(acao)) {
                Produto p = service.criar(req.getParameter("nome"), req.getParameter("categoria"),
                        req.getParameter("fornecedorId"), req.getParameter("preco"),
                        req.getParameter("quantidade"), req.getParameter("unidade"),
                        req.getParameter("descricao"), req.getParameter("foto"), req.getParameter("validade"));
                sucesso(req, "Produto \"" + p.getNome() + "\" criado.");
            } else if ("atualizar".equals(acao)) {
                Produto p = service.atualizar(inteiro(req, "id", -1), req.getParameter("repor"),
                        req.getParameter("novoPreco"));
                sucesso(req, "Produto \"" + p.getNome() + "\" actualizado. Stock: " + p.getQuantidade()
                        + " " + p.getUnidade() + ".");
            } else if ("remover".equals(acao)) {
                service.remover(inteiro(req, "id", -1));
                sucesso(req, "Produto removido.");
            }
        } catch (ValidacaoException e) {
            erro(req, e.getMessage());
        }
        Persistencia.gravarSilencioso();
        redirecionar(req, resp, "/admin/produtos");
    }
}
