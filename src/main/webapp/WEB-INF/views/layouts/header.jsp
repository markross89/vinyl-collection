
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="currentRequestUri"
       value="${not empty requestScope['javax.servlet.forward.request_uri']
       ? requestScope['javax.servlet.forward.request_uri']
       : pageContext.request.requestURI}"/>

<!DOCTYPE html>
<html lang="${pageContext.response.locale.language}">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <meta name="description"
          content="Vinyl Collection - manage and discover vinyl records."/>

    <title>Vinyl Collection</title>

    <link href="${pageContext.request.contextPath}/resources/html-css/vendor/fontawesome-free/css/all.min.css"
          rel="stylesheet"/>

    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@600;700&display=swap"
          rel="stylesheet"/>

    <link href="${pageContext.request.contextPath}/resources/html-css/css/sb-admin-2.min.css"
          rel="stylesheet"/>

    <link href="${pageContext.request.contextPath}/resources/html-css/css/custom.css"
          rel="stylesheet"/>
</head>

<body>

<div class="vc-app">

    <header class="vc-header">

        <!-- Brand -->
        <a class="vc-brand"
           href="${pageContext.request.contextPath}/home/start">

            <span class="vc-brand-icon">
                <i class="fas fa-compact-disc"></i>
            </span>

            <span class="vc-brand-name">
                <spring:message code="app.name"/>
            </span>

        </a>

        <!-- Navigation -->
        <nav class="vc-navigation">

            <sec:authorize access="isAuthenticated()">

                <a href="${pageContext.request.contextPath}/album/albums"
                   class="vc-nav-link">
                    <i class="fas fa-record-vinyl"></i>
                    <spring:message code="nav.albums"/>
                </a>

                <a href="${pageContext.request.contextPath}/track/tracks"
                   class="vc-nav-link">
                    <i class="fas fa-music"></i>
                    <spring:message code="nav.songs"/>
                </a>

                <a href="${pageContext.request.contextPath}/tracklist/tracklists"
                   class="vc-nav-link">
                    <i class="fas fa-list"></i>
                    <spring:message code="nav.playlists"/>
                </a>

                <a href="${pageContext.request.contextPath}/box/boxes"
                   class="vc-nav-link">
                    <i class="fas fa-box"></i>
                    <spring:message code="nav.boxes"/>
                </a>

            </sec:authorize>

            <a href="${pageContext.request.contextPath}/about"
               class="vc-nav-link">
                <spring:message code="nav.about"/>
            </a>

            <a href="${pageContext.request.contextPath}/contact"
               class="vc-nav-link">
                <spring:message code="nav.contact"/>
            </a>

            <sec:authorize access="hasRole('ADMIN')">

                <a href="${pageContext.request.contextPath}/user/userList"
                   class="vc-nav-link vc-admin-link">
                    <i class="fas fa-user-shield"></i>
                    <spring:message code="account.admin"/>
                </a>

            </sec:authorize>

        </nav>

        <!-- Header actions -->
        <div class="vc-header-actions">

            <!-- Search -->
            <form class="vc-search"
                  action="${pageContext.request.contextPath}/home/search"
                  method="get">

                <i class="fas fa-search"></i>

                <input type="text"
                       name="value"
                       value="${fn:escapeXml(param.value)}"
                       placeholder="<spring:message code='search.placeholder'/>"
                       aria-label="<spring:message code='search.ariaLabel'/>"
                       required/>

                <input type="hidden"
                       name="lang"
                       value="${pageContext.response.locale.language}"/>

                <button type="submit" aria-label="Search">
                    <i class="fas fa-arrow-right"></i>
                </button>

            </form>

            <!-- Language selector -->
            <div class="vc-language">

                <form method="get" action="${currentRequestUri}">

                    <input type="hidden" name="lang" value="en"/>

                    <c:if test="${not empty param.value}">
                        <input type="hidden"
                               name="value"
                               value="${fn:escapeXml(param.value)}"/>
                    </c:if>

                    <button type="submit"
                            class="${pageContext.response.locale.language == 'en' ? 'active' : ''}">
                        EN
                    </button>

                </form>

                <form method="get" action="${currentRequestUri}">

                    <input type="hidden" name="lang" value="pl"/>

                    <c:if test="${not empty param.value}">
                        <input type="hidden"
                               name="value"
                               value="${fn:escapeXml(param.value)}"/>
                    </c:if>

                    <button type="submit"
                            class="${pageContext.response.locale.language == 'pl' ? 'active' : ''}">
                        PL
                    </button>

                </form>

            </div>

            <!-- Anonymous user -->
            <sec:authorize access="isAnonymous()">

                <div class="vc-auth-actions">

                    <a href="${pageContext.request.contextPath}/login"
                       class="vc-login-link">
                        <spring:message code="account.login"/>
                    </a>

                </div>

            </sec:authorize>

            <!-- Authenticated user -->
            <sec:authorize access="isAuthenticated()">

                <div class="vc-user-menu">

                    <a href="${pageContext.request.contextPath}/user/editUser"
                       class="vc-user-link">

                        <i class="fas fa-user-circle"></i>

                        <span>
                            <sec:authentication property="principal.username"/>
                        </span>

                    </a>

                    <form action="${pageContext.request.contextPath}/logout"
                          method="post">

                        <c:if test="${not empty _csrf}">
                            <input type="hidden"
                                   name="${_csrf.parameterName}"
                                   value="${_csrf.token}"/>
                        </c:if>

                        <button type="submit" class="vc-logout-button">
                            <i class="fas fa-sign-out-alt"></i>
                            <spring:message code="account.logout"/>
                        </button>

                    </form>

                </div>

            </sec:authorize>

        </div>

    </header>
<%-- SESSION NOTIFICATIONS --%>

<c:set var="loginMessage"
       value="${sessionScope.vcLoginMessage}"/>

<c:set var="albumMessage"
       value="${sessionScope.vcAlbumMessage}"/>

<c:set var="albumMessageType"
       value="${sessionScope.vcAlbumMessageType}"/>

<c:remove var="vcLoginMessage" scope="session"/>
<c:remove var="vcAlbumMessage" scope="session"/>
<c:remove var="vcAlbumMessageType" scope="session"/>

<div id="vc-notification-container">

    <c:if test="${not empty loginMessage}">
        <div class="vc-alert vc-alert-success vc-alert-visible"
             role="status">
            <i class="fas fa-check-circle"></i>
            <span><c:out value="${loginMessage}"/></span>
        </div>
    </c:if>

    <c:if test="${not empty albumMessage}">
        <div class="vc-alert vc-alert-${albumMessageType} vc-alert-visible"
             role="status">
            <c:choose>
                <c:when test="${albumMessageType eq 'success'}">
                    <i class="fas fa-check-circle"></i>
                </c:when>
                <c:when test="${albumMessageType eq 'info'}">
                    <i class="fas fa-info-circle"></i>
                </c:when>
                <c:otherwise>
                    <i class="fas fa-exclamation-circle"></i>
                </c:otherwise>
            </c:choose>

            <span><c:out value="${albumMessage}"/></span>
        </div>
    </c:if>

</div>

<script>
document.addEventListener("DOMContentLoaded", function () {
    const alerts = document.querySelectorAll(
        "#vc-notification-container .vc-alert"
    );

    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.classList.remove("vc-alert-visible");
            alert.classList.add("vc-alert-hiding");

            setTimeout(function () {
                alert.remove();
            }, 400);
        }, 4000);
    });
});
</script>
    <main class="vc-main">
