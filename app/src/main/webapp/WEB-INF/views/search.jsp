<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Search</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet">
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css"/>
</head>

<body>
<%@ include file="menu.jsp" %>
<div class="generic-container">
    <form:form method="POST" modelAttribute="user" class="form-horizontal">
        <%--<form:input type="hidden" path="name" id="user"/>--%>
    <input type="text" placeholder="Search with user's name or telNumber" name="nameOrPhone">
    <button type="submit"><i class="fa fa-search"></i><a href="<c:url value='/list' />"></a></button>
    <div class="has-error">
        <form:errors path="name" class="help-inline"/>
        </form:form>
        </form>
    </div>
</div>
</body>
</html>