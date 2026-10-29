<%@ tag pageEncoding="UTF-8" body-content="empty" description="Cabeçalho de coluna clicável para ordenar" %>
<%@ attribute name="campo" required="true" %>
<%@ attribute name="rotulo" required="true" %>
<%@ attribute name="base" required="true" description="URL sem ordem/dir, ex.: /admin/listagens?tipo=produtos" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="atual" value="${requestScope.ordem == campo}"/>
<c:set var="proxDir" value="${atual && requestScope.dir == 'asc' ? 'desc' : 'asc'}"/>
<c:set var="sep" value="${base.contains('?') ? '&amp;' : '?'}"/>
<a class="ordenar ${atual ? 'ativo' : ''}" href="${pageContext.request.contextPath}${base}${sep}ordem=${campo}&amp;dir=${proxDir}">${rotulo}<c:if test="${atual}"><span class="seta">${requestScope.dir == 'asc' ? '▲' : '▼'}</span></c:if></a>
