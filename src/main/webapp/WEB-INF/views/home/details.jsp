<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Album</h2>
<c:forEach items="${albumDetails}" var="e">
    <h3>${e.value}</h3>
</c:forEach>
<h2>Tracklist :</h2>
<c:forEach items="${tracklist}" var="e">
    <h3>${e.position} ${e.title} ${e.duration}</h3>
</c:forEach>
<h2>Gallery :</h2>
<c:forEach items="${images}" var="e">
    <h3><img src="${e.uri}" alt="${e.type}" width="200" height="200"></h3>
</c:forEach>
<a href="<c:url value="/album/add/${albumDetails.id}"/>">add</a>
</body>
</html>
