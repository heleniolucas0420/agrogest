<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Painel</h1>
        <p class="sub">Resumo do sistema AgroGest.</p>
    </div>
</div>

<div class="estatisticas">
    <a class="stat" href="${ctx}/admin/fornecedores"><span class="stat-valor">${totFornecedores}</span><span class="stat-rotulo">Fornecedores</span></a>
    <a class="stat" href="${ctx}/admin/produtos"><span class="stat-valor">${totProdutos}</span><span class="stat-rotulo">Produtos</span></a>
    <a class="stat" href="${ctx}/admin/listagens?tipo=clientes"><span class="stat-valor">${totClientes}</span><span class="stat-rotulo">Clientes</span></a>
    <a class="stat" href="${ctx}/admin/compras"><span class="stat-valor">${totVendas}</span><span class="stat-rotulo">Vendas</span></a>
    <a class="stat" href="${ctx}/admin/campanhas"><span class="stat-valor">${totCampanhas}</span><span class="stat-rotulo">Campanhas</span></a>
</div>

<div class="estatisticas duas">
    <div class="stat destaque">
        <span class="stat-rotulo">Total facturado (com IVA)</span>
        <span class="stat-valor"><fmt:formatNumber value="${totFacturado}" pattern="#,##0.00"/> MT</span>
    </div>
    <div class="stat">
        <span class="stat-rotulo">Valor total das colheitas</span>
        <span class="stat-valor"><fmt:formatNumber value="${totColheitas}" pattern="#,##0.00"/> MT</span>
    </div>
</div>

<div class="grelha-2">
    <section class="cartao sem-padding">
        <div class="cartao-titulo">
            <h2>Últimas vendas</h2>
            <a href="${ctx}/admin/compras" class="link">Ver todas</a>
        </div>
        <c:choose>
            <c:when test="${empty ultimasVendas}"><div class="vazio pequeno">Ainda não há vendas.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Factura</th><th>Cliente</th><th>Data</th><th class="num">Total</th></tr></thead>
                        <tbody>
                        <c:forEach var="v" items="${ultimasVendas}">
                            <tr>
                                <td data-rotulo="Factura"><a href="${ctx}/factura?id=${v.id}">${v.numeroFactura}</a></td>
                                <td data-rotulo="Cliente"><c:out value="${v.clienteNome}"/></td>
                                <td data-rotulo="Data">${v.dataTexto}</td>
                                <td data-rotulo="Total" class="num"><fmt:formatNumber value="${v.total}" pattern="#,##0.00"/> MT</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo">
            <h2>Stock baixo (≤ 10)</h2>
            <a href="${ctx}/admin/produtos" class="link">Repor stock</a>
        </div>
        <c:choose>
            <c:when test="${empty stockBaixo}"><div class="vazio pequeno">Todos os produtos têm stock suficiente.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Produto</th><th class="num">Stock</th></tr></thead>
                        <tbody>
                        <c:forEach var="p" items="${stockBaixo}">
                            <tr>
                                <td data-rotulo="Produto"><c:out value="${p.nome}"/></td>
                                <td data-rotulo="Stock" class="num"><span class="badge ${p.quantidade == 0 ? 'vermelho' : 'amarelo'}">${p.quantidade} <c:out value="${p.unidade}"/></span></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
