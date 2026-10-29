<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<section class="auth">
    <div class="auth-lado">
        <h1>Gestão agrícola simples, do campo à factura.</h1>
        <p>Fornecedores, campanhas, colheitas e vendas num só lugar.</p>
        <ul class="auth-pontos">
            <li>Catálogo de sementes, fertilizantes e ferramentas</li>
            <li>Facturas com IVA de 16% emitidas na hora</li>
            <li>Relatórios de clientes e vendas</li>
        </ul>
    </div>

    <div class="cartao auth-cartao">
        <h2>Entrar</h2>
        <p class="sub">Use o seu email e palavra-passe.</p>

        <c:if test="${not empty erroLogin}">
            <div class="alerta erro"><c:out value="${erroLogin}"/></div>
        </c:if>

        <form method="post" action="${ctx}/login" class="form">
            <label>Email
                <input type="email" name="email" required autofocus value="<c:out value='${emailDigitado}'/>">
            </label>
            <label>Palavra-passe
                <input type="password" name="password" required>
            </label>
            <button type="submit" class="btn primario largo">Entrar</button>
        </form>

        <p class="centro">Ainda não tem conta? <a href="${ctx}/registo">Registe-se</a></p>

        <details class="credenciais">
            <summary>Credenciais de teste</summary>
            <p><b>Admin:</b> admin@agro.mz / admin234</p>
            <p><b>Cliente:</b> maria@teste.mz / 1234</p>
        </details>
    </div>
</section>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
