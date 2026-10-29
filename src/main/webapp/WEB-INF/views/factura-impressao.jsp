<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Factura ${venda.numeroFactura} · AgroGest</title>
    <link rel="stylesheet" href="${ctx}/css/estilo.css">
</head>
<body class="impressao">
    <div class="barra-impressao">
        <span>Prima <kbd>Ctrl</kbd> + <kbd>P</kbd> (ou <kbd>⌘</kbd> + <kbd>P</kbd> no Mac) para imprimir ou guardar em PDF.</span>
        <a href="${ctx}/factura?id=${venda.id}" class="btn pequeno">Voltar</a>
    </div>
    <%@ include file="/WEB-INF/views/fragments/factura-corpo.jspf" %>
</body>
</html>
