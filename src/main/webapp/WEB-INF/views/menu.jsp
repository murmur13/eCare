<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container-fluid">
<nav class="navbar navbar-inverse">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">

            <div class="navbar-header">
                <a class="navbar-brand" href="#">eCare</a>
            </div>
            <ul class="nav navbar-nav">
                <li class="active"><a href="<c:url value='/mainPage' />">Home</a></li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Users
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/list' />">Users List</a></li>
                        <li><a href="#">Find user</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Tarifs
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='tarifs/listTarifs' />">See all tarifs</a></li>
                    </ul>
                </li>
                <li><a href="#">Options</a></li>
            </ul>
            <%--<span class="caret"></span></a>--%>
            <div class="dropdown float-right">
                <button class="btn btn-primary dropdown-toggle pull-right" type="button" style = "" data-toggle="dropdown">Logout
                    <span class="caret"></span></button>
                <ul class="dropdown-menu dropdown-menu-right">
                    <li><a href="<c:url value='/edit-user-${loggedinuser}' />">Edit your profile</a></li>
                    <li><a href="#" aria-labelledby="dropdownMenuDivider">
                    <li role="separator" class="divider">
                    <li><a href="<c:url value="/logout" />">Logout</a></li>
                </ul>
            </div>
        </div>
        <div class="inverse">
            <%--<%@include file="logout.jsp" %>--%>
        </div>
    </nav>
</nav>
</div><!-- /.container-fluid -->
