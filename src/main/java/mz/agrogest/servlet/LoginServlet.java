package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mz.agrogest.filter.AuthFilter;
import mz.agrogest.model.Utilizador;
import mz.agrogest.service.AuthService;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (utilizador(req) != null) {
            redirecionar(req, resp, "/");
            return;
        }
        vista(req, resp, "login.jsp", "Entrar");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        Utilizador u = auth.autenticar(email, req.getParameter("password"));
        if (u == null) {
            req.setAttribute("erroLogin", "Email ou palavra-passe incorrectos (ou conta inactiva).");
            req.setAttribute("emailDigitado", email);
            vista(req, resp, "login.jsp", "Entrar");
            return;
        }
        // nova sessão depois do login (evita "session fixation")
        HttpSession antiga = req.getSession(false);
        if (antiga != null) {
            antiga.invalidate();
        }
        HttpSession s = req.getSession(true);
        s.setAttribute(AuthFilter.SESSAO_UTILIZADOR, u);
        sucesso(req, "Bem-vindo(a), " + u.getNome() + "!");
        redirecionar(req, resp, u.isAdmin() ? "/admin" : "/catalogo");
    }
}
