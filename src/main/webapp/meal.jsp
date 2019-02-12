<%--
  Created by IntelliJ IDEA.
  User: petrowich
  Date: 12.02.2019
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
</head>
<body>
<c:choose>
    <c:when test="${meal.id == null}">
        <c:set var="submitLable" value="добавить"/>
        <c:set var="idLable" value="NEW"/>
    </c:when>
    <c:otherwise>
        <c:set var="submitLable" value="изменить"/>
        <c:set var="idLable" value="ID: ${meal.id}"/>
    </c:otherwise>
</c:choose>
<form method="post">
    <div><label>${idLable}<input type="hidden" name="id" value="${meal.id}"/></label></div>
    <div><label>Время: <input type="datetime-local" name="datetime" value="${meal.dateTime}"/></label></div>
    <div><label>Описание: <input type="text" name="description" value="${meal.description}"/></label></div>
    <div><label>Калорийность: <input type="number" name="calories" value="${meal.calories}"></label></div>
    <div><button type="submit">${submitLable}</button></div>
</form>
</body>
</html>
