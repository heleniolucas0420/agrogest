<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Relatórios</h1>
        <p class="sub">Clientes e vendas.</p>
    </div>
</div>

<div class="grelha-2">
    <section class="cartao sem-padding">
        <div class="cartao-titulo"><h2>1. Clientes por nome (A → Z)</h2></div>
        <c:choose>
            <c:when test="${empty porNome}"><div class="vazio pequeno">Sem clientes.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Nome</th><th>Email</th><th class="num">Compras</th></tr></thead>
                        <tbody>
                        <c:forEach var="r" items="${porNome}">
                            <tr>
                                <td data-rotulo="Nome"><c:out value="${r.cliente.nome}"/></td>
                                <td data-rotulo="Email"><c:out value="${r.cliente.email}"/></td>
                                <td data-rotulo="Compras" class="num">${r.numeroCompras}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo"><h2>2. Clientes que compraram (por volume)</h2></div>
        <c:choose>
            <c:when test="${empty porVolume}"><div class="vazio pequeno">Ainda ninguém comprou.</div></c:when>
            <c:otherwise>
                <c:set var="maior" value="${porVolume[0].totalGasto}"/>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Cliente</th><th>Volume</th><th class="num">Total gasto</th></tr></thead>
                        <tbody>
                        <c:forEach var="r" items="${porVolume}">
                            <tr>
                                <td data-rotulo="Cliente"><c:out value="${r.cliente.nome}"/></td>
                                <td data-rotulo="Volume" class="barra-celula">
                                    <span class="barra"><span style="width: ${r.totalGasto * 100 / maior}%"></span></span>
                                </td>
                                <td data-rotulo="Total gasto" class="num"><fmt:formatNumber value="${r.totalGasto}" pattern="#,##0.00"/> MT</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo"><h2>3. Clientes mais assíduos</h2></div>
        <c:choose>
            <c:when test="${empty assiduos}"><div class="vazio pequeno">Ainda ninguém comprou.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>#</th><th>Cliente</th><th class="num">N.º compras</th><th class="num">Total gasto</th></tr></thead>
                        <tbody>
                        <c:forEach var="r" items="${assiduos}" varStatus="s">
                            <tr>
                                <td data-rotulo="Posição"><span class="posicao p${s.count}">${s.count}.º</span></td>
                                <td data-rotulo="Cliente"><c:out value="${r.cliente.nome}"/></td>
                                <td data-rotulo="N.º compras" class="num"><b>${r.numeroCompras}</b></td>
                                <td data-rotulo="Total gasto" class="num"><fmt:formatNumber value="${r.totalGasto}" pattern="#,##0.00"/> MT</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo"><h2>Extra: clientes sem compras</h2></div>
        <c:choose>
            <c:when test="${empty semCompras}"><div class="vazio pequeno">Todos os clientes já compraram.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Nome</th><th>Telefone</th><th>Registo</th></tr></thead>
                        <tbody>
                        <c:forEach var="r" items="${semCompras}">
                            <tr>
                                <td data-rotulo="Nome"><c:out value="${r.cliente.nome}"/></td>
                                <td data-rotulo="Telefone"><c:out value="${r.cliente.telefone}"/></td>
                                <td data-rotulo="Registo">${r.cliente.dataRegistoTexto}</td>
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
