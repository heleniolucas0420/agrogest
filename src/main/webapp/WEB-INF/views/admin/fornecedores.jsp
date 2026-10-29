<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Fornecedores</h1>
        <p class="sub">Quem fornece sementes, fertilizantes e maquinaria.</p>
    </div>
</div>

<div class="layout-form">
    <section class="cartao">
        <h2>Novo fornecedor</h2>
        <form method="post" action="${ctx}/admin/fornecedores" class="form">
            <input type="hidden" name="acao" value="criar">
            <label>Nome
                <input type="text" name="nome" required placeholder="Sementes do Sul, Lda">
            </label>
            <label>Tipo
                <select name="tipo" required>
                    <c:forEach var="t" items="${tipos}"><option value="${t}">${t}</option></c:forEach>
                </select>
            </label>
            <label>Contacto
                <input type="text" name="contacto" required placeholder="+258 84 000 0000">
            </label>
            <label>Província
                <input type="text" name="provincia" required placeholder="Maputo">
            </label>
            <button type="submit" class="btn primario largo">Criar fornecedor</button>
        </form>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo"><h2>Lista (${fn:length(fornecedores)})</h2></div>
        <c:choose>
            <c:when test="${empty fornecedores}"><div class="vazio pequeno">Sem fornecedores registados.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>#</th><th>Nome</th><th>Tipo</th><th>Contacto</th><th>Província</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="f" items="${fornecedores}">
                            <tr>
                                <td data-rotulo="ID">${f.id}</td>
                                <td data-rotulo="Nome"><b><c:out value="${f.nome}"/></b></td>
                                <td data-rotulo="Tipo"><span class="badge">${f.tipo}</span></td>
                                <td data-rotulo="Contacto"><c:out value="${f.contacto}"/></td>
                                <td data-rotulo="Província"><c:out value="${f.provincia}"/></td>
                                <td>
                                    <form method="post" action="${ctx}/admin/fornecedores" class="inline">
                                        <input type="hidden" name="acao" value="remover">
                                        <input type="hidden" name="id" value="${f.id}">
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
