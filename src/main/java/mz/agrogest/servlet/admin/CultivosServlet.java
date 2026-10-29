package mz.agrogest.servlet.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.model.Cultivo;
import mz.agrogest.service.CampanhaService;
import mz.agrogest.service.ValidacaoException;
import mz.agrogest.servlet.BaseServlet;

/** Gestão de cultivos por campanha. */
@WebServlet("/admin/cultivos")
public class CultivosServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final CampanhaService service = new CampanhaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int campanhaId = inteiro(req, "campanha", 0);
        List<Cultivo> cultivos = service.listarCultivos(campanhaId);
        Map<Integer, Integer> colheitasPorCultivo = new HashMap<>();
        for (Cultivo c : cultivos) {
            colheitasPorCultivo.put(c.getId(), service.listarColheitas(c.getId()).size());
        }
        req.setAttribute("cultivos", cultivos);
        req.setAttribute("colheitasPorCultivo", colheitasPorCultivo);
        req.setAttribute("campanhas", service.listarCampanhas());
        req.setAttribute("campanhaAtual", campanhaId);
        vista(req, resp, "admin/cultivos.jsp", "Cultivos");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        try {
            if ("criar".equals(acao)) {
                Cultivo c = service.criarCultivo(req.getParameter("campanhaId"), req.getParameter("cultura"),
                        req.getParameter("variedade"), req.getParameter("hectares"),
                        req.getParameter("dataPlantio"));
                sucesso(req, "Cultivo \"" + c.getCultura() + "\" registado em " + c.getCampanhaNome() + ".");
            } else if ("remover".equals(acao)) {
                service.removerCultivo(inteiro(req, "id", -1));
                sucesso(req, "Cultivo removido.");
            }
        } catch (ValidacaoException e) {
            erro(req, e.getMessage());
        }
        Persistencia.gravarSilencioso();
        redirecionar(req, resp, "/admin/cultivos");
    }
}
