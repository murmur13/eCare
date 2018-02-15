<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>User Registration Form</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet">
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
</head>

<body>

<%@ include file="menu.jsp" %>

<div class="generic-container">

    <div class="well lead">Add new Contract</div>
    <form:form method="POST" modelAttribute="contract" class="form-horizontal">
        <form:input type="hidden" path="contractId" id="contract"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="phone">Phone number</label>
                <div class="col-md-7">
                    <form:input type="text" path="tNumber" id="tNumber" name="phone" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="tNumber" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="customer">Customer sso</label>
                <div class="col-md-7">
                    <form:input type="text" path="customer" id="customer" name="customer" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="customer" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="tarif">Tarif name</label>
                <div class="col-md-7">
                    <form:input type="text" path="tarif" name="tarif" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="tarif" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Update" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/contracts/listContracts' />">Cancel</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Register" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/contracts/listContracts' />">Cancel</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
</body>
</html>