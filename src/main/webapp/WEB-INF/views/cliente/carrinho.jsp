<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Carrinho</h1>
        <p class="sub">Reveja os produtos antes de finalizar a compra.</p>
    </div>
    <a href="${ctx}/catalogo" class="btn">Continuar a comprar</a>
</div>

<c:choose>
    <c:when test="${carrinho.vazio}">
        <div class="vazio">
            O carrinho está vazio.
            <a href="${ctx}/catalogo" class="btn primario">Ir ao catálogo</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="layout-checkout">
            <div class="cartao sem-padding">
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead>
                        <tr><th>Produto</th><th class="num">Preço</th><th>Quantidade</th><th class="num">Subtotal</th><th></th></tr>
                        </thead>
                        <tbody>
                        <c:forEach var="i" items="${carrinho.itens}">
                            <tr>
                                <td data-rotulo="Produto"><b><c:out value="${i.nome}"/></b></td>
                                <td data-rotulo="Preço" class="num"><fmt:formatNumber value="${i.preco}" pattern="#,##0.00"/> MT / <c:out value="${i.unidade}"/></td>
                                <td data-rotulo="Quantidade">
                                    <form method="post" action="${ctx}/carrinho/atualizar" class="inline">
                                        <input type="hidden" name="produtoId" value="${i.produtoId}">
                                        <input type="number" name="quantidade" value="${i.quantidade}" min="0" class="qtd">
                                        <button type="submit" class="btn pequeno">Actualizar</button>
                                    </form>
                                </td>
                                <td data-rotulo="Subtotal" class="num"><fmt:formatNumber value="${i.subtotal}" pattern="#,##0.00"/> MT</td>
                                <td>
                                    <form method="post" action="${ctx}/carrinho/remover" class="inline">
                                        <input type="hidden" name="produtoId" value="${i.produtoId}">
                                        <button type="submit" class="btn pequeno perigo">Remover</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <form method="post" action="${ctx}/carrinho/esvaziar" class="rodape-cartao">
                    <button type="submit" class="btn pequeno perigo">Esvaziar carrinho</button>
                </form>
            </div>

            <aside class="cartao resumo">
                <h2>Resumo</h2>
                <div class="resumo-linha"><span>Itens</span><span>${carrinho.totalItens}</span></div>
                <div class="resumo-linha"><span>Subtotal</span><span><fmt:formatNumber value="${carrinho.subtotal}" pattern="#,##0.00"/> MT</span></div>
                <div class="resumo-linha"><span>IVA (16%)</span><span><fmt:formatNumber value="${carrinho.iva}" pattern="#,##0.00"/> MT</span></div>
                <div class="resumo-linha total"><span>Total</span><span><fmt:formatNumber value="${carrinho.total}" pattern="#,##0.00"/> MT</span></div>
                <a href="${ctx}/compra" class="btn primario largo">Finalizar compra</a>
            </aside>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
