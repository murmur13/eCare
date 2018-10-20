<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Users List</title>
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
        <div class="panel-heading"><span class="lead">List of Users </span></div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Firstname</th>
                <th>Lastname</th>
                <th>Email</th>
                <th>SSO ID</th>
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
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.name}</td>
                    <td>${user.surname}</td>
                    <td>${user.mail}</td>
                    <td>${user.ssoId}</td>
                    <td>${user.telNumber}</td>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td><a href="<c:url value='/user/${user.ssoId}/edit' />" class="btn btn-success custom-width">edit</a>
                        </td>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN')">
                        <c:url var="deleteUrl" value="/user/${user.ssoId}/delete"/>
                        <td>
                            <form:form id="${deleteForm}" action="${deleteUrl}" method="POST">
                                <input id="user" name="user" type="hidden" value="${user.ssoId}"/>
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
            <a href="<c:url value='/newuser' />">Add New User</a>
        </div>
    </sec:authorize>

    <ul class="pagination">
        <c:url value="/list" var="prev">
            <c:param name="page" value="${page-1}"/>
        </c:url>
        <c:if test="${page > 1}">
            <li><a href="<c:out value="${prev}" />" class="pn prev">Prev</a></li>
        </c:if>

        <c:forEach begin="1" end="${maxPages}" step="1" varStatus="i">
            <c:choose>
                <c:when test="${page == i.index}">
                    <li class="active"><span>${i.index}</span>
                    <li>
                </c:when>
                <c:otherwise>
                    <c:url value="/list" var="url">
                        <c:param name="page" value="${i.index}"/>
                    </c:url>
                    <li><a href='<c:out value="${url}" />'>${i.index}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:url value="/list" var="next">
            <c:param name="page" value="${page + 1}"/>
        </c:url>
        <c:if test="${page + 1 <= maxPages}">
            <li><a href='<c:out value="${next}" />' class="pn next">Next</a></li>
        </c:if>
    </ul>

</div>
</body>
</html>