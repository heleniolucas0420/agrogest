package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.model.Categoria;
import mz.agrogest.service.ProdutoService;

/** Catálogo de produtos, com filtro por categoria. */
@WebServlet("/catalogo")
public class CatalogoServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final ProdutoService produtos = new ProdutoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Categoria cat = null;
        String c = req.getParameter("categoria");
        if (c != null && !c.isBlank()) {
            try {
                cat = Categoria.valueOf(c);
            } catch (IllegalArgumentException e) {
                cat = null;
            }
        }
        String ordem = req.getParameter("ordem");
        req.setAttribute("produtos", produtos.catalogo(cat, ordem));
        req.setAttribute("categorias", Categoria.values());
        req.setAttribute("categoriaAtual", cat == null ? "" : cat.name());
        req.setAttribute("ordemAtual", ordem == null ? "nome" : ordem);
        vista(req, resp, "cliente/catalogo.jsp", "Catálogo");
    }
}
