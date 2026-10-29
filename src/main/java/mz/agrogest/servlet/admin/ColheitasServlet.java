package mz.agrogest.servlet.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.model.Colheita;
import mz.agrogest.service.CampanhaService;
import mz.agrogest.service.ValidacaoException;
import mz.agrogest.servlet.BaseServlet;
import mz.agrogest.util.Formatos;

/** Gestão de colheitas por cultivo. O valor total é calculado pelo sistema. */
@WebServlet("/admin/colheitas")
public class ColheitasServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private static final Locale PT = Locale.forLanguageTag("pt-PT");
    private final CampanhaService service = new CampanhaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cultivoId = inteiro(req, "cultivo", 0);
        List<Colheita> colheitas = service.listarColheitas(cultivoId);
        double totalKg = 0;
        double totalValor = 0;
        for (Colheita c : colheitas) {
            totalKg += c.getQuantidadeKg();
            totalValor += c.getValorTotal();
        }
        req.setAttribute("colheitas", colheitas);
        req.setAttribute("totalKg", totalKg);
        req.setAttribute("totalValor", Formatos.dinheiro(totalValor));
        req.setAttribute("cultivos", service.listarCultivos(0));
        req.setAttribute("cultivoAtual", cultivoId);
        req.setAttribute("hoje", LocalDate.now().toString());
        vista(req, resp, "admin/colheitas.jsp", "Colheitas");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        try {
            if ("criar".equals(acao)) {
                Colheita c = service.criarColheita(req.getParameter("cultivoId"), req.getParameter("data"),
                        req.getParameter("quantidadeKg"), req.getParameter("precoKg"));
                sucesso(req, String.format(PT, "Colheita registada: %,.0f kg — valor total %,.2f MT.",
                        c.getQuantidadeKg(), c.getValorTotal()));
            } else if ("remover".equals(acao)) {
                service.removerColheita(inteiro(req, "id", -1));
                sucesso(req, "Colheita removida.");
            }
        } catch (ValidacaoException e) {
            erro(req, e.getMessage());
        }
        Persistencia.gravarSilencioso();
        redirecionar(req, resp, "/admin/colheitas");
    }
}
