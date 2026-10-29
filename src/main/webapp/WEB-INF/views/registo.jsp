<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<section class="auth">
    <div class="auth-lado">
        <h1>Crie a sua conta de cliente.</h1>
        <p>Depois do registo pode ver o catálogo, encher o carrinho e comprar.</p>
    </div>

    <div class="cartao auth-cartao">
        <h2>Registo</h2>
        <p class="sub">Todos os campos são obrigatórios.</p>

        <c:if test="${not empty erroForm}">
            <div class="alerta erro"><c:out value="${erroForm}"/></div>
        </c:if>

        <form method="post" action="${ctx}/registo" class="form">
            <label>Nome completo
                <input type="text" name="nome" required value="<c:out value='${param.nome}'/>">
            </label>
            <label>Email
                <input type="email" name="email" required value="<c:out value='${param.email}'/>">
            </label>
            <div class="linha-2">
                <label>Palavra-passe
                    <input type="password" name="password" required minlength="4">
                </label>
                <label>Confirmar
                    <input type="password" name="confirmar" required minlength="4">
                </label>
            </div>
            <label>Telefone
                <input type="tel" name="telefone" required placeholder="+258 84 000 0000" value="<c:out value='${param.telefone}'/>">
            </label>
            <label>Endereço
                <input type="text" name="endereco" required placeholder="Av. Eduardo Mondlane, Nampula" value="<c:out value='${param.endereco}'/>">
            </label>
            <button type="submit" class="btn primario largo">Criar conta</button>
        </form>

        <p class="centro">Já tem conta? <a href="${ctx}/login">Entrar</a></p>
    </div>
</section>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
