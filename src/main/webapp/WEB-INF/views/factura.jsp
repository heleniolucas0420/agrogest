<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Factura ${venda.numeroFactura}</h1>
        <p class="sub">Emitida em ${venda.dataTexto}</p>
    </div>
    <div class="acoes">
        <a href="${ctx}/factura?id=${venda.id}&amp;imprimir=1" class="btn primario" target="_blank">Imprimir factura</a>
        <c:choose>
            <c:when test="${u.admin}"><a href="${ctx}/admin/compras" class="btn">Voltar às compras</a></c:when>
            <c:otherwise><a href="${ctx}/minhas-compras" class="btn">Minhas compras</a></c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/WEB-INF/views/fragments/factura-corpo.jspf" %>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
