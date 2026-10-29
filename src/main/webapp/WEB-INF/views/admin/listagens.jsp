<%@ include file="/WEB-INF/views/fragments/topo.jspf" %>
<c:set var="base" value="/admin/listagens?tipo=${tipo}"/>

<div class="cabecalho">
    <div>
        <h1>Listagens</h1>
        <p class="sub">Clique no nome de uma coluna para ordenar (clique outra vez para inverter).</p>
    </div>
</div>

<nav class="separadores">
    <a href="${ctx}/admin/listagens?tipo=produtos" class="${tipo == 'produtos' ? 'ativo' : ''}">Produtos</a>
    <a href="${ctx}/admin/listagens?tipo=fornecedores" class="${tipo == 'fornecedores' ? 'ativo' : ''}">Fornecedores</a>
    <a href="${ctx}/admin/listagens?tipo=clientes" class="${tipo == 'clientes' ? 'ativo' : ''}">Clientes</a>
    <a href="${ctx}/admin/listagens?tipo=armazem" class="${tipo == 'armazem' ? 'ativo' : ''}">Armazém (stock &gt; 0)</a>
    <a href="${ctx}/admin/compras">Compras efectuadas</a>
</nav>

<section class="cartao sem-padding">
    <div class="cartao-titulo"><h2>${fn:length(lista)} registo(s)</h2></div>
    <c:choose>
        <c:when test="${empty lista}"><div class="vazio pequeno">Sem registos.</div></c:when>

        <c:when test="${tipo == 'fornecedores'}">
            <div class="tabela-wrap">
                <table class="tabela">
                    <thead><tr>
                        <th><ag:ordenar campo="id" rotulo="#" base="${base}"/></th>
                        <th><ag:ordenar campo="nome" rotulo="Nome" base="${base}"/></th>
                        <th><ag:ordenar campo="tipo" rotulo="Tipo" base="${base}"/></th>
                        <th>Contacto</th>
                        <th><ag:ordenar campo="provincia" rotulo="Província" base="${base}"/></th>
                    </tr></thead>
                    <tbody>
                    <c:forEach var="f" items="${lista}">
                        <tr>
                            <td data-rotulo="ID">${f.id}</td>
                            <td data-rotulo="Nome"><b><c:out value="${f.nome}"/></b></td>
                            <td data-rotulo="Tipo"><span class="badge">${f.tipo}</span></td>
                            <td data-rotulo="Contacto"><c:out value="${f.contacto}"/></td>
                            <td data-rotulo="Província"><c:out value="${f.provincia}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>

        <c:when test="${tipo == 'clientes'}">
            <div class="tabela-wrap">
                <table class="tabela">
                    <thead><tr>
                        <th><ag:ordenar campo="nome" rotulo="Nome" base="${base}"/></th>
                        <th><ag:ordenar campo="email" rotulo="Email" base="${base}"/></th>
                        <th>Telefone</th>
                        <th>Endereço</th>
                        <th><ag:ordenar campo="data" rotulo="Registo" base="${base}"/></th>
                        <th>Estado</th>
                        <th></th>
                    </tr></thead>
                    <tbody>
                    <c:forEach var="cl" items="${lista}">
                        <tr>
                            <td data-rotulo="Nome"><b><c:out value="${cl.nome}"/></b></td>
                            <td data-rotulo="Email"><c:out value="${cl.email}"/></td>
                            <td data-rotulo="Telefone"><c:out value="${cl.telefone}"/></td>
                            <td data-rotulo="Endereço"><c:out value="${cl.endereco}"/></td>
                            <td data-rotulo="Registo">${cl.dataRegistoTexto}</td>
                            <td data-rotulo="Estado"><span class="badge ${cl.ativo ? 'verde' : 'cinza'}">${cl.ativo ? 'Activo' : 'Inactivo'}</span></td>
                            <td>
                                <form method="post" action="${ctx}/admin/listagens" class="inline">
                                    <input type="hidden" name="acao" value="alternarCliente">
                                    <input type="hidden" name="id" value="${cl.id}">
                                    <button type="submit" class="btn pequeno ${cl.ativo ? 'perigo' : ''}">${cl.ativo ? 'Desactivar' : 'Activar'}</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>

        <c:otherwise>
            <%-- produtos ou armazém --%>
            <div class="tabela-wrap">
                <table class="tabela">
                    <thead><tr>
                        <th><ag:ordenar campo="nome" rotulo="Nome" base="${base}"/></th>
                        <th><ag:ordenar campo="categoria" rotulo="Categoria" base="${base}"/></th>
                        <th>Fornecedor</th>
                        <th class="num"><ag:ordenar campo="preco" rotulo="Preço" base="${base}"/></th>
                        <th class="num"><ag:ordenar campo="stock" rotulo="Stock" base="${base}"/></th>
                        <th><ag:ordenar campo="validade" rotulo="Validade" base="${base}"/></th>
                        <th class="num">Valor em stock</th>
                    </tr></thead>
                    <tbody>
                    <c:set var="somaStock" value="${0}"/>
                    <c:forEach var="p" items="${lista}">
                        <c:set var="somaStock" value="${somaStock + p.preco * p.quantidade}"/>
                        <tr>
                            <td data-rotulo="Nome"><b><c:out value="${p.nome}"/></b></td>
                            <td data-rotulo="Categoria"><span class="badge">${p.categoria}</span></td>
                            <td data-rotulo="Fornecedor"><c:out value="${p.fornecedorNome}"/></td>
                            <td data-rotulo="Preço" class="num"><fmt:formatNumber value="${p.preco}" pattern="#,##0.00"/> MT</td>
                            <td data-rotulo="Stock" class="num">${p.quantidade} <c:out value="${p.unidade}"/></td>
                            <td data-rotulo="Validade" class="${p.expirado ? 'texto-erro' : ''}">${p.validadeTexto}</td>
                            <td data-rotulo="Valor em stock" class="num"><fmt:formatNumber value="${p.preco * p.quantidade}" pattern="#,##0.00"/> MT</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr><td colspan="6"><b>Valor total em stock</b></td><td class="num"><b><fmt:formatNumber value="${somaStock}" pattern="#,##0.00"/> MT</b></td></tr>
                    </tfoot>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<%@ include file="/WEB-INF/views/fragments/rodape.jspf" %>
