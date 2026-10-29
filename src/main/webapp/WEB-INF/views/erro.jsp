<%@ page isErrorPage="true" %>
<c:set var="codigo" value="${requestScope['jakarta.servlet.error.status_code']}"/>
<c:set var="titulo" value="Erro ${codigo}" scope="request"/>
<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<section class="cartao erro-pagina">
    <div class="erro-codigo">${codigo}</div>
    <c:choose>
        <c:when test="${codigo == 403}">
            <h1>Acesso proibido</h1>
            <p>Não tem permissão para ver esta página.</p>
        </c:when>
        <c:when test="${codigo == 404}">
            <h1>Página não encontrada</h1>
            <p>O endereço que procura não existe.</p>
        </c:when>
        <c:otherwise>
            <h1>Ocorreu um erro</h1>
            <p>Algo correu mal no servidor. Tente novamente.</p>
        </c:otherwise>
    </c:choose>
    <c:if test="${not empty requestScope['jakarta.servlet.error.message'] && codigo != 500}">
        <p class="muted"><c:out value="${requestScope['jakarta.servlet.error.message']}"/></p>
    </c:if>
    <a class="btn primario" href="${ctx}/">Voltar ao início</a>
</section>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
