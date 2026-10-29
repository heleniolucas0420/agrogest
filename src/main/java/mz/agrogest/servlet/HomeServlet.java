package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.model.Utilizador;

/** Página inicial: envia cada perfil para o sítio certo. */
@WebServlet("")
public class HomeServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utilizador u = utilizador(req);
        if (u == null) {
            redirecionar(req, resp, "/login");
        } else if (u.isAdmin()) {
            redirecionar(req, resp, "/admin");
        } else {
            redirecionar(req, resp, "/catalogo");
        }
    }
}
