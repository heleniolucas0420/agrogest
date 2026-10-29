<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Colheitas</h1>
        <p class="sub">O valor total é calculado automaticamente: quantidade (kg) × preço por kg.</p>
    </div>
</div>

<div class="layout-form">
    <section class="cartao">
        <h2>Registar colheita</h2>
        <c:choose>
            <c:when test="${empty cultivos}">
                <div class="alerta aviso">Registe primeiro um cultivo.</div>
                <a href="${ctx}/admin/cultivos" class="btn primario largo">Ir para cultivos</a>
            </c:when>
            <c:otherwise>
                <form method="post" action="${ctx}/admin/colheitas" class="form">
                    <input type="hidden" name="acao" value="criar">
                    <label>Cultivo
                        <select name="cultivoId" required>
                            <c:forEach var="cu" items="${cultivos}">
                                <option value="${cu.id}" ${cu.id == cultivoAtual ? 'selected' : ''}>
                                    <c:out value="${cu.cultura}"/> (<c:out value="${cu.variedade}"/>) — <c:out value="${cu.campanhaNome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </label>
                    <label>Data
                        <input type="date" name="data" required value="${hoje}">
                    </label>
                    <div class="linha-2">
                        <label>Quantidade (kg)
                            <input type="number" name="quantidadeKg" required min="0.01" step="0.01" placeholder="3200">
                        </label>
                        <label>Preço por kg (MT)
                            <input type="number" name="precoKg" required min="0.01" step="0.01" placeholder="22">
                        </label>
                    </div>
                    <button type="submit" class="btn primario largo">Registar colheita</button>
                </form>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo">
            <h2>Lista (${fn:length(colheitas)})</h2>
            <form method="get" action="${ctx}/admin/colheitas" class="inline">
                <select name="cultivo">
                    <option value="0">Todos os cultivos</option>
                    <c:forEach var="cu" items="${cultivos}">
                        <option value="${cu.id}" ${cu.id == cultivoAtual ? 'selected' : ''}><c:out value="${cu.cultura}"/> (<c:out value="${cu.variedade}"/>)</option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn pequeno">Filtrar</button>
            </form>
        </div>
        <c:choose>
            <c:when test="${empty colheitas}"><div class="vazio pequeno">Sem colheitas registadas.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Cultivo</th><th>Data</th><th class="num">Quantidade</th><th class="num">Preço/kg</th><th class="num">Valor total</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="co" items="${colheitas}">
                            <tr>
                                <td data-rotulo="Cultivo"><c:out value="${co.cultivoNome}"/></td>
                                <td data-rotulo="Data">${co.dataTexto}</td>
                                <td data-rotulo="Quantidade" class="num"><fmt:formatNumber value="${co.quantidadeKg}" pattern="#,##0.##"/> kg</td>
                                <td data-rotulo="Preço/kg" class="num"><fmt:formatNumber value="${co.precoKg}" pattern="#,##0.00"/> MT</td>
                                <td data-rotulo="Valor total" class="num"><b><fmt:formatNumber value="${co.valorTotal}" pattern="#,##0.00"/> MT</b></td>
                                <td>
                                    <form method="post" action="${ctx}/admin/colheitas" class="inline">
                                        <input type="hidden" name="acao" value="remover">
                                        <input type="hidden" name="id" value="${co.id}">
                                        <button type="submit" class="btn pequeno perigo">Remover</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot>
                        <tr>
                            <td colspan="2"><b>Total</b></td>
                            <td class="num"><b><fmt:formatNumber value="${totalKg}" pattern="#,##0.##"/> kg</b></td>
                            <td></td>
                            <td class="num"><b><fmt:formatNumber value="${totalValor}" pattern="#,##0.00"/> MT</b></td>
                            <td></td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
