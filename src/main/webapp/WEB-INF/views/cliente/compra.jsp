<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Confirmar compra</h1>
        <p class="sub">Ao confirmar, o stock é descontado e a factura é emitida.</p>
    </div>
</div>

<div class="layout-checkout">
    <div class="cartao">
        <h2>Dados de facturação</h2>
        <dl class="dados">
            <dt>Nome</dt><dd><c:out value="${cliente.nome}"/></dd>
            <dt>Email</dt><dd><c:out value="${cliente.email}"/></dd>
            <dt>Telefone</dt><dd><c:out value="${cliente.telefone}"/></dd>
            <dt>Endereço</dt><dd><c:out value="${cliente.endereco}"/></dd>
        </dl>

        <h2>Produtos</h2>
        <div class="tabela-wrap">
            <table class="tabela">
                <thead><tr><th>Produto</th><th class="num">Qtd.</th><th class="num">Preço unit.</th><th class="num">Subtotal</th></tr></thead>
                <tbody>
                <c:forEach var="i" items="${carrinho.itens}">
                    <tr>
                        <td data-rotulo="Produto"><c:out value="${i.nome}"/></td>
                        <td data-rotulo="Qtd." class="num">${i.quantidade} <c:out value="${i.unidade}"/></td>
                        <td data-rotulo="Preço unit." class="num"><fmt:formatNumber value="${i.preco}" pattern="#,##0.00"/> MT</td>
                        <td data-rotulo="Subtotal" class="num"><fmt:formatNumber value="${i.subtotal}" pattern="#,##0.00"/> MT</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <aside class="cartao resumo">
        <h2>Total a pagar</h2>
        <div class="resumo-linha"><span>Subtotal</span><span><fmt:formatNumber value="${carrinho.subtotal}" pattern="#,##0.00"/> MT</span></div>
        <div class="resumo-linha"><span>IVA (16%)</span><span><fmt:formatNumber value="${carrinho.iva}" pattern="#,##0.00"/> MT</span></div>
        <div class="resumo-linha total"><span>Total</span><span><fmt:formatNumber value="${carrinho.total}" pattern="#,##0.00"/> MT</span></div>
        <form method="post" action="${ctx}/compra">
            <button type="submit" class="btn primario largo">Confirmar e emitir factura</button>
        </form>
        <a href="${ctx}/carrinho" class="btn largo">Voltar ao carrinho</a>
    </aside>
</div>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
