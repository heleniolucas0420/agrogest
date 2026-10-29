package mz.agrogest.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import mz.agrogest.model.Campanha;
import mz.agrogest.model.Carrinho;
import mz.agrogest.model.Cultivo;
import mz.agrogest.model.Fornecedor;
import mz.agrogest.model.Produto;
import mz.agrogest.model.Utilizador;
import mz.agrogest.model.Venda;
import mz.agrogest.service.AuthService;
import mz.agrogest.service.CampanhaService;
import mz.agrogest.service.FornecedorService;
import mz.agrogest.service.ProdutoService;
import mz.agrogest.service.VendaService;

/**
 * Dados de exemplo carregados no primeiro arranque (quando ainda não há ficheiro gravado).
 * Dashboard esperado: 3 fornecedores | 3 produtos | 5 clientes | 4 vendas | 1 campanha.
 */
public final class DadosIniciais {

    private DadosIniciais() {
    }

    public static void carregar() {
        AuthService auth = new AuthService();
        FornecedorService fs = new FornecedorService();
        ProdutoService ps = new ProdutoService();
        CampanhaService cs = new CampanhaService();
        VendaService vs = new VendaService();

        auth.garantirAdmin();

        // Fornecedores
        Fornecedor f1 = fs.criar("Sementes do Norte, Lda", "SEMENTES", "+258 84 123 4567", "Nampula");
        Fornecedor f2 = fs.criar("AgroQuímica Moçambique", "FERTILIZANTES", "+258 82 765 4321", "Maputo");
        Fornecedor f3 = fs.criar("MaquiAgro Centro", "MAQUINARIA", "+258 86 555 1122", "Sofala");

        // Produtos
        LocalDate hoje = LocalDate.now();
        Produto milho = ps.criar("Semente de Milho", "SEMENTE", String.valueOf(f1.getId()), "85", "500",
                "kg", "Semente de milho híbrido, boa para a época chuvosa.", "img/milho.svg",
                hoje.plusMonths(10).toString());
        Produto adubo = ps.criar("Adubo NPK 12-24-12", "FERTILIZANTE", String.valueOf(f2.getId()), "2450",
                "120", "saco", "Saco de 50 kg, ideal para a adubação de fundo.", "img/adubo.svg",
                hoje.plusMonths(18).toString());
        Produto enxada = ps.criar("Enxada Reforçada", "FERRAMENTA", String.valueOf(f3.getId()), "650", "60",
                "unidade", "Enxada de aço com cabo de madeira.", "img/enxada.svg",
                hoje.plusYears(5).toString());

        // Clientes (palavra-passe de todos: 1234)
        Utilizador c1 = auth.registar("Maria Joaquina", "maria@teste.mz", "1234", "+258 84 111 2233",
                "Bairro Muhala, Nampula");
        Utilizador c2 = auth.registar("Carlos Muianga", "carlos@teste.mz", "1234", "+258 82 222 3344",
                "Av. do Trabalho, Nampula");
        Utilizador c3 = auth.registar("Ana Cossa", "ana@teste.mz", "1234", "+258 87 333 4455",
                "Monapo, Nampula");
        auth.registar("Pedro Nhantumbo", "pedro@teste.mz", "1234", "+258 84 444 5566",
                "Ribáuè, Nampula");
        auth.registar("Fátima Abdul", "fatima@teste.mz", "1234", "+258 86 555 6677",
                "Ilha de Moçambique, Nampula");

        // Campanha + cultivo + colheita
        Campanha camp = cs.criarCampanha("Campanha 2025/2026", hoje.minusMonths(10).toString(),
                hoje.plusMonths(2).toString());
        Cultivo cult = cs.criarCultivo(String.valueOf(camp.getId()), "Milho", "Matuba", "5",
                hoje.minusMonths(9).toString());
        cs.criarColheita(String.valueOf(cult.getId()), hoje.minusMonths(4).toString(), "2500", "18");

        // 4 vendas de exemplo (passam pelas mesmas regras de negócio)
        venda(vs, c1, 20, new Object[][]{{milho, 10}, {adubo, 2}});
        venda(vs, c2, 12, new Object[][]{{enxada, 3}});
        venda(vs, c1, 6, new Object[][]{{adubo, 1}, {enxada, 1}});
        venda(vs, c3, 2, new Object[][]{{milho, 25}});
    }

    private static void venda(VendaService vs, Utilizador cliente, int diasAtras, Object[][] itens) {
        Carrinho c = new Carrinho();
        for (Object[] linha : itens) {
            c.adicionar((Produto) linha[0], (Integer) linha[1]);
        }
        Venda v = vs.finalizarCompra(cliente, c);
        v.setData(LocalDateTime.now().minusDays(diasAtras).withSecond(0).withNano(0));
    }
}
