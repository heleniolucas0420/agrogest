package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.model.Carrinho;
import mz.agrogest.model.Utilizador;
import mz.agrogest.model.Venda;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.service.ValidacaoException;
import mz.agrogest.service.VendaService;

/**
 * Finalizar compra.
 *   GET  /compra -> página de confirmação (resumo + IVA)
 *   POST /compra -> confirma: desconta stock e emite a factura
 */
@WebServlet("/compra")
public class CompraServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final VendaService vendas = new VendaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilizador u = utilizador(req);
        if (u.isAdmin()) {
            erro(req, "O administrador não faz compras.");
            redirecionar(req, resp, "/admin");
            return;
        }
        Carrinho c = carrinho(req);
        if (c.isVazio()) {
            erro(req, "O carrinho está vazio.");
            redirecionar(req, resp, "/catalogo");
            return;
        }
        req.setAttribute("carrinho", c);
        req.setAttribute("cliente", u);
        vista(req, resp, "cliente/compra.jsp", "Confirmar compra");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilizador u = utilizador(req);
        try {
            Venda v = vendas.finalizarCompra(u, carrinho(req));
            Persistencia.gravarSilencioso();
            sucesso(req, "Compra concluída! Factura " + v.getNumeroFactura() + " emitida.");
            redirecionar(req, resp, "/factura?id=" + v.getId());
        } catch (ValidacaoException e) {
            erro(req, e.getMessage());
            redirecionar(req, resp, "/carrinho");
        }
    }
}
