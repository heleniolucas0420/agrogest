package mz.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mz.agrogest.filter.AuthFilter;
import mz.agrogest.model.Carrinho;
import mz.agrogest.model.Utilizador;

/** Métodos comuns a todos os controladores (Servlets). */
public abstract class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** Encaminha para uma vista JSP em /WEB-INF/views. */
    protected void vista(HttpServletRequest req, HttpServletResponse resp, String jsp, String titulo)
            throws ServletException, IOException {
        req.setAttribute("titulo", titulo);
        req.getRequestDispatcher("/WEB-INF/views/" + jsp).forward(req, resp);
    }

    protected void redirecionar(HttpServletRequest req, HttpServletResponse resp, String caminho)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + caminho);
    }

    /** Mensagem de sucesso que aparece uma vez na página seguinte. */
    protected void sucesso(HttpServletRequest req, String msg) {
        req.getSession().setAttribute("msg", msg);
    }

    protected void erro(HttpServletRequest req, String msg) {
        req.getSession().setAttribute("erro", msg);
    }

    protected Utilizador utilizador(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (Utilizador) s.getAttribute(AuthFilter.SESSAO_UTILIZADOR);
    }

    protected Carrinho carrinho(HttpServletRequest req) {
        HttpSession s = req.getSession();
        Carrinho c = (Carrinho) s.getAttribute("carrinho");
        if (c == null) {
            c = new Carrinho();
            s.setAttribute("carrinho", c);
        }
        return c;
    }

    protected int inteiro(HttpServletRequest req, String nome, int padrao) {
        try {
            return Integer.parseInt(req.getParameter(nome));
        } catch (NumberFormatException e) {
            return padrao;
        }
    }

    protected boolean desc(HttpServletRequest req) {
        return "desc".equals(req.getParameter("dir"));
    }
}
