<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>User's contract</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet"></link>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css"/>
</head>

<body>
<%@ include file="menu.jsp" %>
<div class="generic-container">
    <div class="panel panel-default">
        <%--<%@include file="authheader.jsp" %>--%>
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Contracts</span></div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Contract Id</th>
                <th>Customer</th>
                <th>Tarif</th>
                <th>Tarif's price (&#8381)</th>
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
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td><a href="<c:url value='/contracts/edit-contract-${contract.contractId}' />"
                               class="btn btn-success custom-width">edit</a></td>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN')">
                        <td><a href="<c:url value='/contracts/${contract.contractId}/deleteContract' />"
                               class="btn btn-danger custom-width">delete</a></td>
                    </sec:authorize>
                    <td><a href="<c:url value='/contracts/${contract.contractId}/block' />"
                           class="btn btn-danger custom-width">block</a></td>
                    <td><a href="<c:url value='/contracts/${contract.contractId}/unblock' />"
                           class="btn btn-success custom-width">unblock</a></td>
                </tr>
            </c:forEach>
            <br>
            <br>
            <br>
            </tbody>
        </table>

        <table class="table table-hover">
            <thead>
            <tr>
                <th>Feature Id</th>
                <th>Feature name</th>
                <th>Feature price (&#8381)</th>
                <th>Connection cost (&#8381)</th>
                <th></th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th width="100"></th>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <th width="100"></th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${userFeatures}" var="feature">
                <tr>
                    <td>${feature.featureId}</td>
                    <td>${feature.featureName}</td>
                    <td>${feature.featurePrice}</td>
                    <td>${feature.connectionCost}</td>
                        <%--<td>${contract.tarif.name}</td>--%>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td><a href="<c:url value='/features/${feature.featureId}/editFeature' />"
                               class="btn btn-success custom-width">edit</a></td>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('USER')">

                        <c:url var="deleteUrl" value="/features/${feature.featureId}/deleteFeatureFromContract"/>
                        <td>
                            <form:form id="${deleteForm}" action="${deleteUrl}" method="POST">
                                <input id="feature" name="feature" type="hidden" value="${feature.featureId}"/>
                                <input type="submit" class="btn btn-danger custom-width" value="delete"
                                       onClick="return confirm('sure?')"/>
                            </form:form>
                        </td>
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

</div>
</body>
</html>