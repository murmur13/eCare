<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Tarifs List</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet"></link>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/js/bootstrap-multiselect.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/css/bootstrap-multiselect.css"/>
</head>

<body>
<%@ include file="menu.jsp" %>
<form:form method="POST" modelAttribute="tarif" class="form-horizontal">
<div class="generic-container">
        <%--<div class="panel panel-default">--%>
    <div class="row">
        <div class="col-md-2">
            <div class="form-group">
                <td>List of tarifs:<td>
                <form:select class="form-control" path="tarifId" id="tarifId">
                <c:forEach items="${tarifs}" var="tarif">
                <form:option value="${tarif.tarifId}">${tarif.name} &#8195 &#8195 &#8195 &#8195 &#8195 ${tarif.price}</form:option>
                </c:forEach>
                </form:select>
            </div>
        </div>
            <%--</div>--%>
    </div>
    <div class="row">
        <div class="form-actions">
            <input type="submit" value="Save tarif to contract" class="btn btn-primary btn-sm"/>
            <td><a href=="<c:url value='/contracts/edit-contractTarif-${tarif.tarifId}' />"></a></td>
            <input type="submit" value="Cancel" class="btn btn-warning btn-sm"/>
            <a href="contractslist.jsp"></a>
                <%--href="<c:url value='/contracts/listContracts' />"></a>--%>
        </div>
    </div>
    </form:form>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#example-getting-started').multiselect({
            maxHeight: 400
        });
    });
    $('#submit').click(function () {
        $('.formSubmit').each(function () {
            $(this).submit();
        });
    });
</script>
</div>
</body>

</html>
