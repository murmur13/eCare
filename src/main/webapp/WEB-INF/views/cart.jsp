<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Cart</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet"></link>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" rel="stylesheet"
          type="text/css">

    <link href="http://example.com/myicon.png" rel="icon" type="image/x-icon"/>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet"></link>
    <link href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" rel="stylesheet"
          type="text/css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/css/bootstrap-multiselect.css"
          rel="stylesheet">
    <%--<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">--%>
    <%--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>--%>
    <%--<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>
    <%--<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.15/js/bootstrap-multiselect.min.js"></script>--%>
    <%--<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>--%>
    <%--<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>--%>

</head>

<body>
<%@ include file="menu.jsp" %>
<div class="container">
    <div class="row">
        <div class="col-xs-8">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <div class="panel-title">
                        <div class="row">
                            <div class="col-xs-6">
                                <h5><span class="glyphicon glyphicon-shopping-cart"></span> Cart</h5>
                            </div>
                            <div class="col-xs-6">
                                <button type="button" class="btn btn-warning btn-sm btn-block">
                                    <a href="<c:url value='/features/listFeatures' />">
                                        <span class="glyphicon glyphicon-share-alt"></span>See other options to choose
                                    </a>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-2"><img class="img-responsive" src="http://placehold.it/100x70">
                        </div>
                        <div class="col-xs-4">
                            <h4 class="product-name"><strong>
                                <td>${sessionScope.cart.tarifInCart.name}</td>
                            </strong></h4>
                            <h4>
                                <small>Tarif description</small>
                            </h4>
                        </div>
                        <div class="col-xs-6">
                            <div class="col-xs-6 text-right">
                                <h6><strong>${sessionScope.cart.tarifInCart.price}<span
                                        class="text-muted"></span></strong></h6>
                            </div>
                            <div class="col-xs-2">
                                <button type="button" class="btn btn-link btn-xs" style="float:right">
                                    <a href="<c:url value='/cart/deleteTarif' />">
                                    <span class="glyphicon glyphicon-trash"> </span>
                                    </a>
                                </button>
                            </div>
                        </div>
                    </div>
                    <hr>
                    <br/>


                    <c:if test="${sessionScope.cart.optionsInCart!=null}">
                        <c:forEach items="${sessionScope.cart.optionsInCart}" var="feature">
                    <thead>
                    <th>Option name</th>
                    <th>Option price (&#8381)</th>
                    <th>Connection cost (&#8381)</th>
                    </thead>
                            <div class="row">
                                <div class="col-xs-2"><img class="img-responsive" src="http://placehold.it/100x70">
                                </div>
                                <div class="col-xs-4">
                                    <tr>

                                    <tr>
                                            <%--<td>${feature.featureId}</td>--%>
                                                <td><c:out value="${feature.featureName}"/></td>
                                                <td>${it.feature.featurePrice}</td>
                                                <td>${feature.connectionCost}</td>
                                    </tr>
                                    <h4 class="product-name"><strong>${it.feature.featureName}</strong></h4>
                                    <%--<h4>--%>
                                        <%--<small>Option description</small>--%>
                                    <%--</h4>--%>

                                    <div class="col-xs-6">
                                        <div class="col-xs-6 text-right">
                                            <h6><strong>${feature.featurePrice}<span class="text-muted"></span></strong>
                                            </h6>
                                        </div>
                                        <div class="col-xs-2">
                                            <button type="button" class="btn btn-link btn-xs" style="float:right">
                                                <span class="glyphicon glyphicon-trash"> </span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>


                    <br/>


                    <hr>
                    <div class="row">
                        <div class="text-center">
                            <div class="col-xs-9">
                                <h6 class="text-right">Added items?</h6>
                            </div>
                            <div class="col-xs-3">
                                <button type="button" class="btn btn-default btn-sm btn-block">
                                    <a href="<c:url value='/cart/refresh' />"></a>
                                    Update cart
                                </button>
                            </div>
                        </div>
                    </div>
                    <%--</div>--%>
                    <div class="panel-footer">
                        <div class="row text-center">
                            <div class="col-xs-9">
                                <h4 class="text-right">Total <strong>$50.00</strong></h4>
                            </div>
                            <div class="col-xs-3">
                                <button type="button" class="btn btn-success btn-block">
                                    <a href="<c:url value='/cart/submit' />">
                                    Submit
                                    </a>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>