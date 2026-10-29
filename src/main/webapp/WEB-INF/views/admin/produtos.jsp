<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Produtos</h1>
        <p class="sub">Catálogo e stock. Para repor stock, indique a quantidade a somar.</p>
    </div>
</div>

<div class="layout-form">
    <section class="cartao">
        <h2>Novo produto</h2>
        <c:choose>
            <c:when test="${empty fornecedores}">
                <div class="alerta aviso">Crie primeiro um fornecedor.</div>
                <a href="${ctx}/admin/fornecedores" class="btn primario largo">Ir para fornecedores</a>
            </c:when>
            <c:otherwise>
                <form method="post" action="${ctx}/admin/produtos" class="form">
                    <input type="hidden" name="acao" value="criar">
                    <label>Nome
                        <input type="text" name="nome" required placeholder="Semente Feijão nhemba">
                    </label>
                    <div class="linha-2">
                        <label>Categoria
                            <select name="categoria" required>
                                <c:forEach var="c" items="${categorias}"><option value="${c}">${c}</option></c:forEach>
                            </select>
                        </label>
                        <label>Fornecedor
                            <select name="fornecedorId" required>
                                <c:forEach var="f" items="${fornecedores}"><option value="${f.id}"><c:out value="${f.nome}"/></option></c:forEach>
                            </select>
                        </label>
                    </div>
                    <div class="linha-3">
                        <label>Preço (MT)
                            <input type="number" name="preco" required min="0.01" step="0.01" placeholder="320">
                        </label>
                        <label>Quantidade
                            <input type="number" name="quantidade" required min="0" step="1" placeholder="400">
                        </label>
                        <label>Unidade
                            <input type="text" name="unidade" required placeholder="kg" list="unidades">
                        </label>
                    </div>
                    <datalist id="unidades">
                        <option value="kg"><option value="saco"><option value="unidade"><option value="litro"><option value="caixa">
                    </datalist>
                    <label>Validade
                        <input type="date" name="validade" required value="${validadePadrao}">
                    </label>
                    <label>Descrição
                        <textarea name="descricao" rows="2" placeholder="Breve descrição do produto"></textarea>
                    </label>
                    <label>Foto (URL)
                        <input type="text" name="foto" placeholder="https://... ou img/milho.svg">
                    </label>
                    <button type="submit" class="btn primario largo">Criar produto</button>
                </form>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="cartao sem-padding">
        <div class="cartao-titulo"><h2>Lista (${fn:length(produtos)})</h2></div>
        <c:choose>
            <c:when test="${empty produtos}"><div class="vazio pequeno">Sem produtos registados.</div></c:when>
            <c:otherwise>
                <div class="tabela-wrap">
                    <table class="tabela">
                        <thead><tr><th>Produto</th><th class="num">Preço</th><th class="num">Stock</th><th>Validade</th><th>Repor stock / preço</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="p" items="${produtos}">
                            <tr>
                                <td data-rotulo="Produto">
                                    <b><c:out value="${p.nome}"/></b><br>
                                    <small class="muted">${p.categoria} · <c:out value="${p.fornecedorNome}"/></small>
                                </td>
                                <td data-rotulo="Preço" class="num"><fmt:formatNumber value="${p.preco}" pattern="#,##0.00"/> MT</td>
                                <td data-rotulo="Stock" class="num">
                                    <span class="badge ${p.quantidade == 0 ? 'vermelho' : (p.quantidade <= 10 ? 'amarelo' : 'verde')}">${p.quantidade} <c:out value="${p.unidade}"/></span>
                                </td>
                                <td data-rotulo="Validade" class="${p.expirado ? 'texto-erro' : ''}">${p.validadeTexto}</td>
                                <td data-rotulo="Actualizar">
                                    <form method="post" action="${ctx}/admin/produtos" class="inline">
                                        <input type="hidden" name="acao" value="atualizar">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <input type="number" name="repor" min="1" placeholder="+ qtd" class="qtd">
                                        <input type="number" name="novoPreco" min="0.01" step="0.01" placeholder="preço" class="qtd">
                                        <button type="submit" class="btn pequeno">Guardar</button>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" action="${ctx}/admin/produtos" class="inline">
                                        <input type="hidden" name="acao" value="remover">
                                        <input type="hidden" name="id" value="${p.id}">
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
