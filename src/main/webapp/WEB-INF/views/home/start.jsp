<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form   method="post">
    First name: <input type="text" name="dada"><br>

   <button type="submit">dcd</button>
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