<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Persistência</h1>
        <p class="sub">Os dados vivem em memória (RAM). O estado é gravado em ficheiro ao encerrar o Tomcat e restaurado no arranque.</p>
    </div>
</div>

<div class="grelha-2">
    <section class="cartao">
        <h2>Ficheiro de dados</h2>
        <dl class="dados">
            <dt>Caminho</dt><dd><code><c:out value="${ficheiro}"/></code></dd>
            <dt>Estado</dt>
            <dd><span class="badge ${existe ? 'verde' : 'cinza'}">${existe ? 'Gravado' : 'Ainda não gravado'}</span></dd>
            <dt>Tamanho</dt><dd><fmt:formatNumber value="${tamanho / 1024}" pattern="#,##0.0"/> KB</dd>
            <dt>Última gravação</dt><dd>${ultimaGravacao}</dd>
        </dl>
    </section>

    <section class="cartao">
        <h2>Acções</h2>
        <form method="post" action="${ctx}/admin/persistencia" class="form">
            <input type="hidden" name="acao" value="gravar">
            <button type="submit" class="btn primario largo">Gravar agora</button>
        </form>
        <form method="post" action="${ctx}/admin/persistencia" class="form">
            <input type="hidden" name="acao" value="eliminar">
            <button type="submit" class="btn perigo largo">Eliminar ficheiro gravado</button>
        </form>
        <p class="muted pequeno-texto">
            A gravação é atómica: escreve primeiro num ficheiro <code>.tmp</code>, guarda a versão anterior em
            <code>.bak</code> e só depois substitui o ficheiro. Se o ficheiro principal estiver corrompido,
            o sistema usa a cópia <code>.bak</code>.
        </p>
    </section>
</div>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
