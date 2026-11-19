
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <title>Vinyl Collection - <spring:message code="login.title"/></title>

    <link href="${pageContext.request.contextPath}/resources/html-css/vendor/fontawesome-free/css/all.min.css"
          rel="stylesheet"/>

    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@600;700&display=swap"
          rel="stylesheet"/>

    <link href="${pageContext.request.contextPath}/resources/html-css/css/sb-admin-2.min.css"
          rel="stylesheet"/>

    <link href="${pageContext.request.contextPath}/resources/html-css/css/custom.css"
          rel="stylesheet"/>
</head>


<body class="vc-login-body">

<div class="vc-login-wrapper">

    <div class="vc-login-top">

        <a href="<c:url value='/home/start'/>" class="vc-login-brand">
            <i class="fas fa-compact-disc"></i>
            <span>VINYL COLLECTION</span>
        </a>

        <div class="vc-login-language">
            <a href="?lang=en" class="vc-login-language-link">EN</a>
            <span>/</span>
            <a href="?lang=pl" class="vc-login-language-link">PL</a>
        </div>

    </div>

    <div class="vc-login-card">

        <div class="vc-login-icon">
            <i class="fas fa-record-vinyl"></i>
        </div>

        <div class="vc-login-heading">
            <span class="vc-eyebrow">
                <spring:message code="login.eyebrow"/>
            </span>

            <h1>
                <spring:message code="login.title"/>
            </h1>

            <p>
                <spring:message code="login.subtitle"/>
            </p>
        </div>

        <c:if test="${not empty param.error}">
            <div class="vc-login-error" role="alert">
                <i class="fas fa-exclamation-circle"></i>
                <spring:message code="login.error"/>
            </div>
        </c:if>

        <form action="<c:url value='/login'/>"
              method="post"
              class="vc-login-form">

              <c:if test="${not empty param.addAlbumId}">
                  <input type="hidden"
                         name="addAlbumId"
                         value="<c:out value='${param.addAlbumId}'/>"/>
              </c:if>

            <div class="vc-login-field">

                <label for="username">
                    <spring:message code="login.username"/>
                </label>

                <div class="vc-login-input-wrapper">
                    <i class="fas fa-user"></i>

                   <input type="email"
                          id="username"
                          name="username"
                          placeholder="<spring:message code='login.username.placeholder'/>"
                          autocomplete="username"
                          input mode="email"
                          maxlength="254"
                          required/>
                </div>

            </div>

            <div class="vc-login-field">

                <label for="password">
                    <spring:message code="login.password"/>
                </label>

                <div class="vc-login-input-wrapper">
                    <i class="fas fa-lock"></i>

                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="<spring:message code='login.password.placeholder'/>"
                           autocomplete="current-password"
                           required/>
                </div>

            </div>

            <c:if test="${not empty _csrf}">
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}"/>
            </c:if>

            <button type="submit" class="vc-login-button">
                <spring:message code="login.button"/>
                <i class="fas fa-arrow-right"></i>
            </button>

        </form>

        <div class="vc-login-divider"></div>

        <p class="vc-login-register">
            <spring:message code="login.noAccount"/>
            <a href="<c:url value='/user/register'/>">
                <spring:message code="login.register"/>
            </a>
        </p>

    </div>

    <c:url var="backUrl" value="/home/start"/>

    <c:if test="${not empty param.returnUrl}">
        <c:url var="backUrl" value="${param.returnUrl}"/>
    </c:if>

    <a href="<c:out value='${backUrl}'/>" class="vc-login-back">
        <i class="fas fa-arrow-left"></i>
        <spring:message code="common.home"/>
    </a>

</div>
<div id="vc-notification-container"></div>

<c:if test="${pageContext.request.getParameter('error') != null}">
    <spring:message code="login.error" var="loginErrorMessage"/>

    <div id="vc-flash-notification"
         data-message="${fn:escapeXml(loginErrorMessage)}"
         data-type="error">
    </div>
</c:if>

<c:if test="${empty param.error and not empty notificationMessage}">
    <spring:message code="${notificationMessage}" var="flashMessage"/>

    <div id="vc-flash-notification"
         data-message="${fn:escapeXml(flashMessage)}"
         data-type="${notificationType}">
    </div>
</c:if>

<script src="${pageContext.request.contextPath}/resources/html-css/js/notifications.js"></script>
</body>
</html>
