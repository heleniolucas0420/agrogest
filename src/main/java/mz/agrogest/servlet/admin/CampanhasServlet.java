package mz.agrogest.servlet.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.model.Campanha;
import mz.agrogest.service.CampanhaService;
import mz.agrogest.service.ValidacaoException;
import mz.agrogest.servlet.BaseServlet;

/** Gestão de campanhas: criar, fechar, reabrir, remover (se não tiver cultivos). */
@WebServlet("/admin/campanhas")
public class CampanhasServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final CampanhaService service = new CampanhaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Integer, Long> cultivosPorCampanha = new HashMap<>();
        for (Campanha c : service.listarCampanhas()) {
            cultivosPorCampanha.put(c.getId(), service.totalCultivos(c.getId()));
        }
        req.setAttribute("campanhas", service.listarCampanhas());
        req.setAttribute("cultivosPorCampanha", cultivosPorCampanha);
        vista(req, resp, "admin/campanhas.jsp", "Campanhas");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        int id = inteiro(req, "id", -1);
        try {
            if ("criar".equals(acao)) {
                Campanha c = service.criarCampanha(req.getParameter("nome"), req.getParameter("inicio"),
                        req.getParameter("fim"));
                sucesso(req, "Campanha \"" + c.getNome() + "\" criada.");
            } else if ("fechar".equals(acao)) {
                service.alterarEstado(id, false);
                sucesso(req, "Campanha fechada.");
            } else if ("reabrir".equals(acao)) {
                service.alterarEstado(id, true);
                sucesso(req, "Campanha reaberta.");
            } else if ("remover".equals(acao)) {
                service.removerCampanha(id);
                sucesso(req, "Campanha removida.");
            }
        } catch (ValidacaoException e) {
            erro(req, e.getMessage());
        }
        Persistencia.gravarSilencioso();
        redirecionar(req, resp, "/admin/campanhas");
    }
}
