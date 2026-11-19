
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>

<section class="vc-page vc-about-page">

    <div class="vc-page-header">
        <span class="vc-eyebrow">
            <spring:message code="about.eyebrow"/>
        </span>

        <h1>
            <spring:message code="about.title"/>
        </h1>
    </div>

    <div class="vc-about-content">
        <div class="vc-about-record">
            <i class="fas fa-compact-disc"></i>
        </div>

        <div class="vc-about-description">
            <p>
                <spring:message code="about.text"/>
            </p>

            <a href="${pageContext.request.contextPath}/contact"
               class="vc-button vc-button-primary">
                <spring:message code="about.contact.button"/>
                <i class="fas fa-arrow-right"></i>
            </a>
        </div>
    </div>

</section>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
