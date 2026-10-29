<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Campanhas</h1>
        <p class="sub">Uma campanha com cultivos não pode ser removida — feche-a em vez disso.</p>
    </div>
</div>

<div class="layout-form">
    <section class="cartao">
        <h2>Nova campanha</h2>
        <form method="post" action="${ctx}/admin/campanhas" class="form">
            <input type="hidden" name="acao" value="criar">
            <label>Nome
                <input type="text" name="nome" required placeholder="Campanha 2026/2027">
            </label>
            <div class="linha-2">
                <label>Início
                    <input type="date" name="inicio" required>
                </label>
                <label>Fim
                    <input type="date" name="fim" required>
                </label>
            </div>
            <button type="submit" class="btn primario largo">Criar campanha</button>
        </form>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo"><h2>Lista (${fn:length(campanhas)})</h2></div>
        <c:choose>
            <c:when test="${empty campanhas}"><div class="vazio pequeno">Sem campanhas registadas.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Campanha</th><th>Período</th><th>Estado</th><th class="num">Cultivos</th><th>Acções</th></tr></thead>
                        <tbody>
                        <c:forEach var="c" items="${campanhas}">
                            <tr>
                                <td data-rotulo="Campanha"><b><c:out value="${c.nome}"/></b></td>
                                <td data-rotulo="Período">${c.inicioTexto} → ${c.fimTexto}</td>
                                <td data-rotulo="Estado">
                                    <span class="badge ${c.aberta ? 'verde' : 'cinza'}">${c.aberta ? 'Aberta' : 'Fechada'}</span>
                                </td>
                                <td data-rotulo="Cultivos" class="num">
                                    <a href="${ctx}/admin/cultivos?campanha=${c.id}">${cultivosPorCampanha[c.id]}</a>
                                </td>
                                <td class="acoes-linha">
                                    <form method="post" action="${ctx}/admin/campanhas" class="inline">
                                        <input type="hidden" name="id" value="${c.id}">
                                        <input type="hidden" name="acao" value="${c.aberta ? 'fechar' : 'reabrir'}">
                                        <button type="submit" class="btn pequeno">${c.aberta ? 'Fechar' : 'Reabrir'}</button>
                                    </form>
                                    <form method="post" action="${ctx}/admin/campanhas" class="inline">
                                        <input type="hidden" name="id" value="${c.id}">
                                        <input type="hidden" name="acao" value="remover">
                                        <button type="submit" class="btn pequeno perigo">Remover</button>
                                    </form>
                                </td>
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
