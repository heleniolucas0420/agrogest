package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.service.AuthService;
import mz.agrogest.service.ValidacaoException;

/** Auto-registo de clientes. */
@WebServlet("/registo")
public class RegistoServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        vista(req, resp, "registo.jsp", "Criar conta");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pass = req.getParameter("password");
        String conf = req.getParameter("confirmar");
        try {
            if (pass != null && !pass.equals(conf)) {
                throw new ValidacaoException("As palavras-passe não coincidem.");
            }
            auth.registar(req.getParameter("nome"), req.getParameter("email"), pass,
                    req.getParameter("telefone"), req.getParameter("endereco"));
            Persistencia.gravarSilencioso();
            sucesso(req, "Conta criada com sucesso. Já pode entrar.");
            redirecionar(req, resp, "/login");
        } catch (ValidacaoException e) {
            req.setAttribute("erroForm", e.getMessage());
            vista(req, resp, "registo.jsp", "Criar conta");
        }
    }
}
