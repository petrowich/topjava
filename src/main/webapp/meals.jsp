<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.util.LocalDateTimeUtil" %>
<html>
<head>
    <title>Meals</title>
    <style type="text/css">
        thead th {text-align: left}
        tbody tr td {text-align: left}
        .excess {color: red;}
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table width="800">
    <thead>
        <tr>
            <th>дата</th>
            <th>время</th>
            <th>описание</th>
            <th>калории</th>
            <th></th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <c:set var="dateTimeFormatter" value="${dateTimeFormatter}"/>
    <c:forEach items="${mealToList}" var="mealTo">
        <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr ${mealTo.isExcess() ? 'class=\"excess\"' : null}>
            <td><%=LocalDateTimeUtil.localDateToString(mealTo.getDateTime())%></td>
            <td><%=LocalDateTimeUtil.localTimeToString(mealTo.getDateTime())%></td>
            <td>${mealTo.getDescription()}</td>
            <td>${mealTo.getCalories()}</td>
            <td><a href="${pageContext.request.contextPath}/meals?editmeal=${mealTo.id}">редактировать</a> </td>
            <td><a href="${pageContext.request.contextPath}/meals?delmeal=${mealTo.id}">удалить</a> </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="${pageContext.request.contextPath}/meals?editmeal=new">добаивить</a></p>
</body>
</html>
