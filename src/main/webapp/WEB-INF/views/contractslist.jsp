<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Users List</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet"></link>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
</head>

<body>
<%@ include file="menu.jsp" %>
<div class="generic-container">
    <div class="panel panel-default">
        <div class="panel-heading"><span class="lead">Contracts</span></div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Contract Id</th>
                <th>Customer</th>
                <th>Tarif</th>
                <th>Tarif's price (&#8381)</th>
                <th>Phone</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th width="100"></th>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <th width="100"></th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${contracts}" var="contract">
                <tr>
                    <td>${contract.contractId}</td>
                    <td>${contract.customer.ssoId}</td>
                    <td>${contract.tarif.name}</td>
                    <td>${contract.tarif.price}</td>
                    <td>${contract.tNumber}</td>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td><a href="<c:url value='/contracts/edit-contractTarif-${contract.contractId}' />" class="btn btn-primary custom-width">change tarif</a></td>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td><a href="<c:url value='/contracts/edit-contractOptions-${contract.contractId}' />" class="btn btn-primary custom-width">change options</a></td>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN')">
                        <td><a href="<c:url value='/contracts/delete-contract-${contract.contractId}' />" class="btn btn-danger custom-width">delete</a></td>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td><a href="<c:url value='/contracts/showOptions-${contract.contractId}' />" class="btn btn-warning custom-width">show chosen options</a></td>
                    </sec:authorize>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <sec:authorize access="hasRole('ADMIN')">
        <div class="well">
            <a href="<c:url value='/contracts/newcontract' />">Add New Contract</a>
        </div>
    </sec:authorize>

    <ul class="pagination">
        <c:url value="/contracts/listContracts" var="prev">
            <c:param name="page" value="${page-1}"/>
        </c:url>
        <c:if test="${page > 1}">
        <li> <a href="<c:out value="${prev}" />" class="pn prev">Prev</a></li>
        </c:if>

        <c:forEach begin="1" end="${maxPages}" step="1" varStatus="i">
            <c:choose>
                <c:when test="${page == i.index}">
                    <li class="active"><span>${i.index}</span><li>
                </c:when>
                <c:otherwise>
                    <c:url value="/contracts/listContracts" var="url">
                        <c:param name="page" value="${i.index}"/>
                    </c:url>
             <li><a href='<c:out value="${url}" />'>${i.index}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:url value="/contracts/listContracts" var="next">
            <c:param name="page" value="${page + 1}"/>
        </c:url>
        <c:if test="${page + 1 <= maxPages}">
        <li><a href='<c:out value="${next}" />' class="pn next">Next</a></li>
        </c:if>
    </ul>

</div>
</body>
</html>