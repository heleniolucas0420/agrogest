<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Minhas compras</h1>
        <p class="sub">Histórico de todas as suas compras e facturas.</p>
    </div>
</div>

<c:choose>
    <c:when test="${empty vendas}">
        <div class="vazio">
            Ainda não fez nenhuma compra.
            <a href="${ctx}/catalogo" class="btn primario">Ir ao catálogo</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="cartao sem-padding">
            <div class="tabela-wrap">
                <table class="tabela">
                    <thead>
                    <tr><th>Factura</th><th>Data</th><th>Produtos</th><th class="num">Total</th><th></th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="v" items="${vendas}">
                        <tr>
                            <td data-rotulo="Factura"><b>${v.numeroFactura}</b></td>
                            <td data-rotulo="Data">${v.dataTexto}</td>
                            <td data-rotulo="Produtos">
                                <c:forEach var="i" items="${v.itens}" varStatus="s">
                                    <c:out value="${i.produtoNome}"/> (${i.quantidade})<c:if test="${!s.last}">, </c:if>
                                </c:forEach>
                            </td>
                            <td data-rotulo="Total" class="num"><b><fmt:formatNumber value="${v.total}" pattern="#,##0.00"/> MT</b></td>
                            <td><a href="${ctx}/factura?id=${v.id}" class="btn pequeno">Ver factura</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
