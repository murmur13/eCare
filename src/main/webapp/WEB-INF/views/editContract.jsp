<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
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
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/css/bootstrap-multiselect.css" />
</head>

<body>
<%@ include file="menu.jsp" %>
<form:form method="POST" modelAttribute="contract" class="form-horizontal">
<form:input type="hidden" path="contractId" id="contract"/>
<div class="generic-container">
    <div class="panel panel-default">
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label for="listOfTarifs">List of tarifs:</label>
                    <select class="form-control" id="listOfTarifs">
                        <c:forEach items="${tarifs}" var="tarif">
                            <c:choose>
                                <c:when test="${tarif.tarifId == userTarif.tarifId}">
                                    <option selected>${tarif.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option>${tarif.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-md-8">
                <div class="form-group">
                    <label for="example-getting-started">List of features:</label>
                    <select id="example-getting-started" multiple="multiple">
                        <c:forEach items="${features}" var="feature">
                            <c:forEach items="${contractFeatures}" var="contractFeature">
                                <c:choose>
                                    <c:when test="${feature.equals(contractFeature)}">
                                        <option selected
                                                value="${contractFeature.featureId}">${contractFeature.featureName}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${feature.featureId}">${feature.featureName}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="form-actions">
            <c:choose>
                <c:when test="${edit}">
                    <input type="submit" value="Update" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/contracts/edit-contract-${contract.contractId}' />">Update</a>
                </c:when>
                <c:otherwise>
                    <input type="submit" value="Cancel" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/contracts/listContracts' />">Cancel</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    </form:form>
    </div>

        <%--<td><a href="<c:url value='/contracts/edit-contract-${contract.contractId}' />" class="btn btn-success custom-width">Save changes</a></td>--%>
        <%--<td><a href="<c:url value='/contracts/listContracts' />" class="btn btn-primary custom-width">Cancel</a></td>--%>

    <script type="text/javascript">
        $(document).ready(function () {
            $('#example-getting-started').multiselect({
                maxHeight: 400
            });
        });
    </script>
</div>
</body>

</html>