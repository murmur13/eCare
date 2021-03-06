<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>User details</title>
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
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">User details </span></div>
        <table class="table table-hover">
            <thead>
            <tr>
            <tr>
                <th>Contract Id</th>
                <th>Customer</th>
                <th>Tarif</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th width="100"></th>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <th width="100"></th>
                </sec:authorize>

            </tr>
            <%--<sec:authorize access="hasRole('USER') or hasRole('DBA')">--%>
            <%--<jsp:forward page="main.jsp"/>--%>
            <%--</sec:authorize>--%>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th width="100"></th>
            </sec:authorize>
            <sec:authorize access="hasRole('ADMIN')">
                <th width="100"></th>
            </sec:authorize>

            </tr>
            </thead>
            <tbody>

            <%--<c:forEach items="${contract}" var="contract">--%>
            <h1>User Detail</h1>
            <br />

            <div class="row">
                <label class="col-sm-2">User ID</label>
                <div class="col-sm-10">${user.customerId}</div>
            </div>

            <div class="row">
                <label class="col-sm-2">ssoId</label>
                <div class="col-sm-10">${user.ssoId}</div>
            </div>

            <div class="row">
                <label class="col-sm-2">name</label>
                <div class="col-sm-10">${user.name}</div>
            </div>

            <div class="row">
                <label class="col-sm-2">surname</label>
                <div class="col-sm-10">${user.surname}</div>
            </div>

            <div class="row">
                <label class="col-sm-2">birthDate</label>
                <div class="col-sm-10">${user.birthDate}</div>
            </div>

            <div class="row">
                <label class="col-sm-2">phone number</label>
                <div class="col-sm-10">${user.telNumber}</div>
            </div>

            <div class="row">
                <label class="col-sm-2">mail</label>
                <div class="col-sm-10">${user.mail}</div>
            </div>

            <%--</c:forEach>--%>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>