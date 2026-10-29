package mz.agrogest.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.servlet.BaseServlet;

/** Endpoint de persistência: gravar agora / eliminar o ficheiro gravado. */
@WebServlet("/admin/persistencia")
public class PersistenciaServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("ficheiro", Persistencia.ficheiro().toString());
        req.setAttribute("existe", Persistencia.existe());
        req.setAttribute("tamanho", Persistencia.tamanhoBytes());
        req.setAttribute("ultimaGravacao", Persistencia.ultimaGravacao());
        vista(req, resp, "admin/persistencia.jsp", "Persistência");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        try {
            if ("gravar".equals(acao)) {
                Persistencia.gravar();
                sucesso(req, "Estado do sistema gravado em ficheiro.");
            } else if ("eliminar".equals(acao)) {
                boolean apagou = Persistencia.eliminar();
                sucesso(req, (apagou ? "Ficheiro eliminado." : "Não havia ficheiro gravado.")
                        + " Os dados em memória continuam até reiniciar o Tomcat; no próximo arranque"
                        + " o sistema volta aos dados de exemplo (use «Gravar agora» para cancelar).");
            }
        } catch (IOException e) {
            erro(req, "Erro de ficheiro: " + e.getMessage());
        }
        redirecionar(req, resp, "/admin/persistencia");
    }
}
