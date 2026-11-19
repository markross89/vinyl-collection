
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:message code="register.firstName.placeholder" var="firstNamePlaceholder"/>
<spring:message code="register.lastName.placeholder" var="lastNamePlaceholder"/>
<spring:message code="register.email.placeholder" var="emailPlaceholder"/>
<spring:message code="profile.edit.password.placeholder" var="passwordPlaceholder"/>
<spring:message code="register.confirmPassword.placeholder" var="confirmPasswordPlaceholder"/>
<spring:message code="profile.delete.confirm" var="deleteConfirm"/>

<!DOCTYPE html>
<html lang="${pageContext.response.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><spring:message code="profile.edit.title"/></title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@600;700&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/html-css/css/sb-admin-2.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/html-css/css/custom.css">
</head>

<body class="vc-login-body">

<div class="vc-login-wrapper">

    <div class="vc-login-top">
        <a href="${pageContext.request.contextPath}/home/start"
           class="vc-login-brand">
            <i class="fas fa-record-vinyl"></i>
            <span>Vinyl Collection</span>
        </a>

        <div class="vc-login-language">
            <a href="?lang=en">EN</a>
            <span>|</span>
            <a href="?lang=pl">PL</a>
        </div>
    </div>

    <div class="vc-login-card">

        <div class="vc-login-icon">
            <i class="fas fa-user-edit"></i>
        </div>

        <div class="vc-login-heading">
            <span class="vc-login-eyebrow">
                <spring:message code="profile.edit.eyebrow"/>
            </span>

            <h1><spring:message code="profile.edit.title"/></h1>

            <p><spring:message code="profile.edit.subtitle"/></p>
        </div>

        <form:form action="${pageContext.request.contextPath}/user/editUser"
                   method="post"
                   modelAttribute="userDto"
                   cssClass="vc-login-form">

            <div class="vc-login-field">
                <label for="firstName">
                    <spring:message code="register.firstName"/>
                </label>

                <div class="vc-login-input-wrapper">
                    <i class="fas fa-user"></i>
                    <form:input path="firstName"
                                id="firstName"
                                placeholder="${firstNamePlaceholder}"
                                autocomplete="given-name"/>
                </div>

                <form:errors path="firstName" cssClass="vc-login-error"/>
            </div>

            <div class="vc-login-field">
                <label for="lastName">
                    <spring:message code="register.lastName"/>
                </label>

                <div class="vc-login-input-wrapper">
                    <i class="fas fa-user"></i>
                    <form:input path="lastName"
                                id="lastName"
                                placeholder="${lastNamePlaceholder}"
                                autocomplete="family-name"/>
                </div>

                <form:errors path="lastName" cssClass="vc-login-error"/>
            </div>

            <div class="vc-login-field">
                <label for="username">
                    <spring:message code="register.email"/>
                </label>

                <div class="vc-login-input-wrapper">
                    <i class="fas fa-envelope"></i>
                    <form:input path="username"
                                id="username"
                                type="email"
                                placeholder="${emailPlaceholder}"
                                autocomplete="email"/>
                </div>

                <form:errors path="username" cssClass="vc-login-error"/>
            </div>

            <div class="vc-login-field">
                <label for="password">
                    <spring:message code="profile.edit.password"/>
                </label>

                <div class="vc-login-input-wrapper">
                    <i class="fas fa-lock"></i>
                    <form:password path="password"
                                   id="password"
                                   placeholder="${passwordPlaceholder}"
                                   autocomplete="new-password"/>
                </div>

                <form:errors path="password" cssClass="vc-login-error"/>
            </div>

            <div class="vc-login-field">
                <label for="confirmPassword">
                    <spring:message code="register.confirmPassword"/>
                </label>

                <div class="vc-login-input-wrapper">
                    <i class="fas fa-lock"></i>
                    <form:password path="confirmPassword"
                                   id="confirmPassword"
                                   placeholder="${confirmPasswordPlaceholder}"
                                   autocomplete="new-password"/>
                </div>

                <form:errors path="confirmPassword" cssClass="vc-login-error"/>
            </div>

            <c:if test="${not empty _csrf}">
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}"/>
            </c:if>

            <button type="submit" class="vc-login-button">
                <spring:message code="profile.edit.button"/>
                <i class="fas fa-save"></i>
            </button>

        </form:form>

        <a href="#"
           class="vc-login-back"
           onclick="history.back(); return false;">

            <i class="fas fa-arrow-left"></i>
            <spring:message code="common.back"/>

        </a>
        <div class="vc-login-delete-account">

            <div class="vc-login-delete-heading">
                <h3>
                    <spring:message code="profile.delete.title"/>
                </h3>

                <p>
                    <spring:message code="profile.delete.description"/>
                </p>
            </div>

           <form id="deleteAccountForm"
                 action="${pageContext.request.contextPath}/user/deleteAccount"
                 method="post">

               <c:if test="${not empty _csrf}">
                   <input type="hidden"
                          name="${_csrf.parameterName}"
                          value="${_csrf.token}"/>
               </c:if>

               <button type="button"
                       class="vc-login-delete-button"
                       id="openDeleteAccountModal">
                   <spring:message code="profile.delete.button"/>
                   <i class="fas fa-trash-alt"></i>
               </button>
           </form>

        </div>



    </div>
</div>
<div id="vc-notification-container"></div>

<c:if test="${not empty notificationMessage}">
    <spring:message code="${notificationMessage}" var="flashMessage"/>
    <div id="vc-flash-notification"
         data-message="${fn:escapeXml(flashMessage)}"
         data-type="${notificationType}">
    </div>
</c:if>

<jsp:include page="/WEB-INF/views/login/delete-account-modal.jsp"/>

<script src="${pageContext.request.contextPath}/resources/html-css/js/delete-account-modal.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/js/notifications.js"></script>

</body>
</html>
