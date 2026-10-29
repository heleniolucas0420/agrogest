<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Cultivos</h1>
        <p class="sub">Cultivos registados por campanha. Só campanhas abertas aceitam novos cultivos.</p>
    </div>
</div>

<div class="layout-form">
    <section class="cartao">
        <h2>Registar cultivo</h2>
        <c:choose>
            <c:when test="${empty campanhas}">
                <div class="alerta aviso">Crie primeiro uma campanha.</div>
                <a href="${ctx}/admin/campanhas" class="btn primario largo">Ir para campanhas</a>
            </c:when>
            <c:otherwise>
                <form method="post" action="${ctx}/admin/cultivos" class="form">
                    <input type="hidden" name="acao" value="criar">
                    <label>Campanha
                        <select name="campanhaId" required>
                            <c:forEach var="c" items="${campanhas}">
                                <option value="${c.id}" ${c.id == campanhaAtual ? 'selected' : ''} ${c.aberta ? '' : 'disabled'}>
                                    <c:out value="${c.nome}"/>${c.aberta ? '' : ' (fechada)'}
                                </option>
                            </c:forEach>
                        </select>
                    </label>
                    <div class="linha-2">
                        <label>Cultura
                            <input type="text" name="cultura" required placeholder="Feijão nhemba">
                        </label>
                        <label>Variedade
                            <input type="text" name="variedade" required placeholder="Macia">
                        </label>
                    </div>
                    <div class="linha-2">
                        <label>Hectares
                            <input type="number" name="hectares" required min="0.01" step="0.01" placeholder="8">
                        </label>
                        <label>Data de plantio
                            <input type="date" name="dataPlantio" required>
                        </label>
                    </div>
                    <button type="submit" class="btn primario largo">Registar cultivo</button>
                </form>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo">
            <h2>Lista (${fn:length(cultivos)})</h2>
            <form method="get" action="${ctx}/admin/cultivos" class="inline">
                <select name="campanha">
                    <option value="0">Todas as campanhas</option>
                    <c:forEach var="c" items="${campanhas}">
                        <option value="${c.id}" ${c.id == campanhaAtual ? 'selected' : ''}><c:out value="${c.nome}"/></option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn pequeno">Filtrar</button>
            </form>
        </div>
        <c:choose>
            <c:when test="${empty cultivos}"><div class="vazio pequeno">Sem cultivos registados.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Cultura</th><th>Campanha</th><th class="num">Hectares</th><th>Plantio</th><th class="num">Colheitas</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="cu" items="${cultivos}">
                            <tr>
                                <td data-rotulo="Cultura"><b><c:out value="${cu.cultura}"/></b><br><small class="muted"><c:out value="${cu.variedade}"/></small></td>
                                <td data-rotulo="Campanha"><c:out value="${cu.campanhaNome}"/></td>
                                <td data-rotulo="Hectares" class="num"><fmt:formatNumber value="${cu.hectares}" pattern="#,##0.##"/> ha</td>
                                <td data-rotulo="Plantio">${cu.dataPlantioTexto}</td>
                                <td data-rotulo="Colheitas" class="num"><a href="${ctx}/admin/colheitas?cultivo=${cu.id}">${colheitasPorCultivo[cu.id]}</a></td>
                                <td>
                                    <form method="post" action="${ctx}/admin/cultivos" class="inline">
                                        <input type="hidden" name="acao" value="remover">
                                        <input type="hidden" name="id" value="${cu.id}">
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
