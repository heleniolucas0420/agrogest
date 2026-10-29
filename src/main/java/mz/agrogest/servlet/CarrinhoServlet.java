package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.model.Carrinho;
import mz.agrogest.model.Produto;
import mz.agrogest.model.Utilizador;
import mz.agrogest.service.ProdutoService;
import mz.agrogest.service.ValidacaoException;
import mz.agrogest.service.VendaService;

/**
 * Carrinho de compras (fica na sessão HTTP).
 *   GET  /carrinho            -> ver carrinho
 *   POST /carrinho/adicionar  -> adicionar produto
 *   POST /carrinho/atualizar  -> alterar quantidade
 *   POST /carrinho/remover    -> remover linha
 *   POST /carrinho/esvaziar   -> limpar tudo
 */
@WebServlet(urlPatterns = {"/carrinho", "/carrinho/*"})
public class CarrinhoServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final ProdutoService produtos = new ProdutoService();
    private final VendaService vendas = new VendaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (bloquearAdmin(req, resp)) {
            return;
        }
        Carrinho c = carrinho(req);
        req.setAttribute("carrinho", c);
        vista(req, resp, "cliente/carrinho.jsp", "Carrinho");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (bloquearAdmin(req, resp)) {
            return;
        }
        String acao = req.getPathInfo() == null ? "" : req.getPathInfo();
        Carrinho c = carrinho(req);
        int produtoId = inteiro(req, "produtoId", -1);
        String voltar = "/carrinho";
        try {
            switch (acao) {
                case "/adicionar": {
                    int qtd = inteiro(req, "quantidade", 1);
                    Produto p = produtos.buscar(produtoId);
                    if (p == null) {
                        throw new ValidacaoException("Produto não encontrado.");
                    }
                    if (qtd <= 0) {
                        throw new ValidacaoException("A quantidade deve ser maior que zero.");
                    }
                    vendas.verificarDisponibilidade(produtoId, c.quantidadeDe(produtoId) + qtd);
                    c.adicionar(p, qtd);
                    sucesso(req, qtd + " " + p.getUnidade() + " de \"" + p.getNome() + "\" adicionado ao carrinho.");
                    if ("catalogo".equals(req.getParameter("origem"))) {
                        voltar = "/catalogo";
                    }
                    break;
                }
                case "/atualizar": {
                    int qtd = inteiro(req, "quantidade", 0);
                    if (qtd > 0) {
                        vendas.verificarDisponibilidade(produtoId, qtd);
                    }
                    c.atualizar(produtoId, qtd);
                    sucesso(req, qtd > 0 ? "Quantidade actualizada." : "Item removido do carrinho.");
                    break;
                }
                case "/remover":
                    c.remover(produtoId);
                    sucesso(req, "Item removido do carrinho.");
                    break;
                case "/esvaziar":
                    c.esvaziar();
                    sucesso(req, "Carrinho esvaziado.");
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
            }
        } catch (ValidacaoException e) {
            erro(req, e.getMessage());
            if ("catalogo".equals(req.getParameter("origem"))) {
                voltar = "/catalogo";
            }
        }
        redirecionar(req, resp, voltar);
    }

    private boolean bloquearAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utilizador u = utilizador(req);
        if (u != null && u.isAdmin()) {
            erro(req, "O administrador não faz compras. Entre com uma conta de cliente.");
            redirecionar(req, resp, "/admin");
            return true;
        }
        return false;
    }
}
