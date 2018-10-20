<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>eCare</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet">
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet">
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>

    <div class="well lead">Add new Option</div>
    <form:form method="POST" modelAttribute="feature" class="form-horizontal">
        <form:input type="hidden" path="featureId" id="feature"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="featureName">Name</label>
                <div class="col-md-7">
                    <form:input type="text" path="featureName" id="featureName" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="featureName" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="featureName">Price</label>
                <div class="col-md-7">
                    <form:input type="text" path="featurePrice" id="featurePrice" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="featurePrice" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="featureName">Connection cost</label>
                <div class="col-md-7">
                    <form:input type="text" path="connectionCost" id="connectionCost" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="connectionCost" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Update" class="btn btn-primary btn-sm"/> or <a
                            href="<c:url value='/features/listFeatures' />">Cancel</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Register" class="btn btn-primary btn-sm"/> or <a
                            href="<c:url value='/features/listFeatures' />">Cancel</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
</body>
</html>