package mz.agrogest.servlet.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.agrogest.model.Produto;
import mz.agrogest.model.Venda;
import mz.agrogest.repo.DataStore;
import mz.agrogest.service.CampanhaService;
import mz.agrogest.service.ClienteService;
import mz.agrogest.service.FornecedorService;
import mz.agrogest.service.ProdutoService;
import mz.agrogest.service.VendaService;
import mz.agrogest.servlet.BaseServlet;

/** Painel do administrador com os totais do sistema. */
@WebServlet("/admin")
public class DashboardServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private final FornecedorService fornecedores = new FornecedorService();
    private final ProdutoService produtos = new ProdutoService();
    private final ClienteService clientes = new ClienteService();
    private final VendaService vendas = new VendaService();
    private final CampanhaService campanhas = new CampanhaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("totFornecedores", fornecedores.listar("nome", false).size());
        req.setAttribute("totProdutos", produtos.listar("nome", false).size());
        req.setAttribute("totClientes", clientes.listarClientes("nome", false).size());
        req.setAttribute("totVendas", DataStore.get().vendas.size());
        req.setAttribute("totCampanhas", campanhas.listarCampanhas().size());
        req.setAttribute("totFacturado", vendas.totalFacturado());
        req.setAttribute("totColheitas", campanhas.valorTotalColheitas());

        // últimas 5 vendas
        List<Venda> ultimas = vendas.listarTodas("data", true);
        req.setAttribute("ultimasVendas", ultimas.subList(0, Math.min(5, ultimas.size())));

        // produtos com stock baixo (<= 10)
        List<Produto> baixo = produtos.listar("stock", false);
        baixo.removeIf(p -> p.getQuantidade() > 10);
        req.setAttribute("stockBaixo", baixo);

        vista(req, resp, "admin/dashboard.jsp", "Painel");
    }
}
