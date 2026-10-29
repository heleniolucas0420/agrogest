<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>

<div class="cabecalho">
    <div>
        <h1>Catálogo</h1>
        <p class="sub">Sementes, fertilizantes, maquinaria e ferramentas disponíveis.</p>
    </div>
    <c:if test="${sessionScope.carrinho != null && !sessionScope.carrinho.vazio}">
        <a href="${ctx}/carrinho" class="btn primario">Ver carrinho (${sessionScope.carrinho.totalItens})</a>
    </c:if>
</div>

<form method="get" action="${ctx}/catalogo" class="filtros">
    <div class="chips">
        <a href="${ctx}/catalogo?ordem=${ordemAtual}" class="chip ${empty categoriaAtual ? 'ativo' : ''}">Todas</a>
        <c:forEach var="cat" items="${categorias}">
            <a href="${ctx}/catalogo?categoria=${cat}&ordem=${ordemAtual}" class="chip ${categoriaAtual == cat.name() ? 'ativo' : ''}">
                ${fn:substring(cat, 0, 1)}${fn:toLowerCase(fn:substring(cat, 1, 20))}
            </a>
        </c:forEach>
    </div>
    <label class="ordem">Ordenar
        <select name="ordem">
            <option value="nome" ${ordemAtual == 'nome' ? 'selected' : ''}>Nome</option>
            <option value="preco" ${ordemAtual == 'preco' ? 'selected' : ''}>Preço</option>
            <option value="stock" ${ordemAtual == 'stock' ? 'selected' : ''}>Stock</option>
            <option value="validade" ${ordemAtual == 'validade' ? 'selected' : ''}>Validade</option>
        </select>
    </label>
    <input type="hidden" name="categoria" value="${categoriaAtual}">
    <button type="submit" class="btn">Aplicar</button>
</form>

<c:choose>
    <c:when test="${empty produtos}">
        <div class="vazio">Não há produtos nesta categoria.</div>
    </c:when>
    <c:otherwise>
        <div class="grelha-produtos">
            <c:forEach var="p" items="${produtos}">
                <article class="produto ${p.emStock && !p.expirado ? '' : 'esgotado'}">
                    <div class="produto-foto">
                        <c:choose>
                            <c:when test="${empty p.foto}"><img src="${ctx}/img/produto.svg" alt=""></c:when>
                            <c:when test="${p.fotoExterna}"><img src="<c:out value='${p.foto}'/>" alt="<c:out value='${p.nome}'/>"></c:when>
                            <c:otherwise><img src="${ctx}/<c:out value='${p.foto}'/>" alt="<c:out value='${p.nome}'/>"></c:otherwise>
                        </c:choose>
                        <span class="etiqueta cat-${fn:toLowerCase(p.categoria)}">${p.categoria}</span>
                    </div>
                    <div class="produto-corpo">
                        <h3><c:out value="${p.nome}"/></h3>
                        <p class="descricao"><c:out value="${p.descricao}"/></p>
                        <dl class="detalhes">
                            <dt>Fornecedor</dt><dd><c:out value="${p.fornecedorNome}"/></dd>
                            <dt>Validade</dt><dd class="${p.expirado ? 'texto-erro' : ''}">${p.validadeTexto}</dd>
                            <dt>Disponível</dt>
                            <dd>
                                <c:choose>
                                    <c:when test="${p.expirado}"><span class="texto-erro">Fora da validade</span></c:when>
                                    <c:when test="${p.emStock}">${p.quantidade} <c:out value="${p.unidade}"/></c:when>
                                    <c:otherwise><span class="texto-erro">Esgotado</span></c:otherwise>
                                </c:choose>
                            </dd>
                        </dl>
                        <div class="preco">
                            <fmt:formatNumber value="${p.preco}" pattern="#,##0.00"/> MT
                            <small>/ <c:out value="${p.unidade}"/></small>
                        </div>
                        <c:if test="${p.emStock && !p.expirado}">
                            <form method="post" action="${ctx}/carrinho/adicionar" class="adicionar">
                                <input type="hidden" name="produtoId" value="${p.id}">
                                <input type="hidden" name="origem" value="catalogo">
                                <input type="number" name="quantidade" value="1" min="1" max="${p.quantidade}" aria-label="Quantidade">
                                <button type="submit" class="btn primario">Adicionar</button>
                            </form>
                        </c:if>
                    </div>
                </article>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
