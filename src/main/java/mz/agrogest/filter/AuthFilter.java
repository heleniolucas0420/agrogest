package mz.agrogest.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mz.agrogest.model.Utilizador;

/**
 * Controlo de acesso por sessão e por perfil.
 *  - RN d: /admin/* só para o administrador (HTTP 403 caso contrário).
 *  - RN e: /catalogo, /carrinho/*, /compra, /factura, /minhas-compras exigem sessão.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    public static final String SESSAO_UTILIZADOR = "utilizador";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");

        String caminho = req.getRequestURI().substring(req.getContextPath().length());
        HttpSession sessao = req.getSession(false);
        Utilizador u = sessao == null ? null : (Utilizador) sessao.getAttribute(SESSAO_UTILIZADOR);

        if (caminho.equals("/admin") || caminho.startsWith("/admin/")) {
            if (u == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            if (!u.isAdmin()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Área reservada ao administrador.");
                return;
            }
        } else if (exigeSessao(caminho) && u == null) {
            if (sessao == null) {
                sessao = req.getSession(true);
            }
            sessao.setAttribute("erro", "Inicie sessão para continuar.");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean exigeSessao(String c) {
        return c.equals("/catalogo")
                || c.equals("/carrinho") || c.startsWith("/carrinho/")
                || c.equals("/compra")
                || c.equals("/factura")
                || c.equals("/minhas-compras");
    }
}
