<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<%@include file="authheader.jsp" %>
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
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Users
                        <span class="caret"></span></a>
                    </sec:authorize>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/list' />">Users List</a></li>
                        <li><a href="<c:url value='/search' />">Find user</a></li>
                    </ul>
                </li>


                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Tarifs
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/tarifs/listTarifs' />">See all tarifs</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">Options
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/features/listFeatures' />">See all options</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Contracts
                            <span class="caret"></span></a>
                    </sec:authorize>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/contracts/listContracts' />">See all contracts</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <sec:authorize access="hasRole('USER')">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">My Contract
                            <span class="caret"></span></a>
                    </sec:authorize>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/contracts/getMyContract' />">See my contracts</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <sec:authorize access="hasRole('ADMIN')">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Blocking options
                            <span class="caret"></span></a>
                    </sec:authorize>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/features/blockingFeatures/seeAll' />">See blocked options</a></li>
                        <li><a href="<c:url value='/features/blockingFeatures' />">Block options</a></li>
                        <li><a href="<c:url value='/features/unblockFeatures' />">Unblock options</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <sec:authorize access="hasRole('ADMIN')">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Required options
                            <span class="caret"></span></a>
                    </sec:authorize>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/features/requiredFeatures' />">Make required options</a></li>
                        <li><a href="<c:url value='/features/dismissRequiredFeatures' />">Dismiss required options</a></li>
                        <li><a href="<c:url value='/features/requiredFeatures/seeAll' />">See required options</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <sec:authorize access="hasRole('USER')">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">My Cart
                            <span class="caret"></span></a>
                    </sec:authorize>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value='/cart' />">Go to cart</a></li>
                    </ul>
                </li>

            </ul>
            <%--<span class="caret"></span></a>--%>
            <div class="dropdown float-right">
                <button class="btn btn-primary dropdown-toggle pull-right logout-button" type="button" data-toggle="dropdown">${loggedinuser}
                    <span class="caret"></span></button>
                <ul class="dropdown-menu dropdown-menu-right">
                    <li><a href="<c:url value='/edit-user-${loggedinuser}' />">Edit your profile</a></li>
                    <li><a href="#" aria-labelledby="dropdownMenuDivider">
                    <li role="separator" class="divider">
                    <li><a href="<c:url value="/logout" />">Logout</a></li>
                </ul>
            </div>
        </div>
        <%--<div class="inverse">--%>
            <%--&lt;%&ndash;<%@include file="logout.jsp" %>&ndash;%&gt;--%>
        <%--</div>--%>
    </nav>
</nav>
</div><!-- /.container-fluid -->
