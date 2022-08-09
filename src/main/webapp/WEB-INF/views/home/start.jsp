<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form   method="post">
    First name: <input type="text" name="data"><br>

   <button type="submit">dcd</button>
</form>
<a href="<c:url value="/security/login"/>">login</a>
<form action="<c:url value="/logout"/>" method="post">
    <input class="fa fa-id-badge" type="submit" value="Wyloguj">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
<table>
    <c:forEach items="${thumbs}" var="e">
        <tr>
            <td><a href="<c:url value="/home/details/${e.id}"/>"><img src="${e.image}" alt="${e.title}" width="200" height="200"></a><td>
            <td>${e.title}<td>
        </tr>
    </c:forEach>
</table>
</body>
</html>