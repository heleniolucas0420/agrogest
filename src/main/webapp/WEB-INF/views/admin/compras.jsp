<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>
<c:set var="base" value="/admin/compras"/>

<div class="cabecalho">
    <div>
        <h1>Compras detalhadas</h1>
        <p class="sub">${fn:length(vendas)} compra(s) · Total facturado: <b><fmt:formatNumber value="${totalFacturado}" pattern="#,##0.00"/> MT</b></p>
    </div>
    <div class="ordenacao">
        Ordenar por:
        <ag:ordenar campo="data" rotulo="Data" base="${base}"/>
        <ag:ordenar campo="valor" rotulo="Valor" base="${base}"/>
        <ag:ordenar campo="cliente" rotulo="Cliente" base="${base}"/>
    </div>
</div>

<c:if test="${empty vendas}">
    <div class="vazio">Ainda não há compras.</div>
</c:if>

<div class="lista-compras">
    <c:forEach var="v" items="${vendas}">
        <article class="cartao compra">
            <header class="compra-topo">
                <div>
                    <a href="${ctx}/factura?id=${v.id}" class="compra-num">${v.numeroFactura}</a>
                    <span class="muted">· ${v.dataTexto}</span>
                </div>
                <div class="compra-total"><fmt:formatNumber value="${v.total}" pattern="#,##0.00"/> MT</div>
            </header>
            <div class="compra-cliente">
                <div><span class="muted">Cliente</span><b><c:out value="${v.clienteNome}"/></b></div>
                <div><span class="muted">Endereço</span><c:out value="${v.clienteEndereco}"/></div>
                <div><span class="muted">Telefone</span><c:out value="${v.clienteTelefone}"/></div>
            </div>
            <div class="tabela-wrap">
                <table class="tabela compacta">
                    <thead><tr><th>Produto</th><th class="num">Quantidade</th><th class="num">Valor unitário</th><th class="num">Subtotal</th></tr></thead>
                    <tbody>
                    <c:forEach var="i" items="${v.itens}">
                        <tr>
                            <td data-rotulo="Produto"><c:out value="${i.produtoNome}"/></td>
                            <td data-rotulo="Quantidade" class="num">${i.quantidade} <c:out value="${i.unidade}"/></td>
                            <td data-rotulo="Valor unitário" class="num"><fmt:formatNumber value="${i.precoUnit}" pattern="#,##0.00"/> MT</td>
                            <td data-rotulo="Subtotal" class="num"><fmt:formatNumber value="${i.subtotal}" pattern="#,##0.00"/> MT</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr><td colspan="3" class="num">Subtotal</td><td class="num"><fmt:formatNumber value="${v.subtotal}" pattern="#,##0.00"/> MT</td></tr>
                    <tr><td colspan="3" class="num">IVA (16%)</td><td class="num"><fmt:formatNumber value="${v.iva}" pattern="#,##0.00"/> MT</td></tr>
                    <tr><td colspan="3" class="num"><b>Total</b></td><td class="num"><b><fmt:formatNumber value="${v.total}" pattern="#,##0.00"/> MT</b></td></tr>
                    </tfoot>
                </table>
            </div>
        </article>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
