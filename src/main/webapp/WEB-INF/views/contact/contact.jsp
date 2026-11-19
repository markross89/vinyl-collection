
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>

<section class="vc-page vc-contact-page">

    <div class="vc-page-header">
        <span class="vc-eyebrow">
            <spring:message code="contact.eyebrow"/>
        </span>

        <h1>
            <spring:message code="contact.title"/>
        </h1>

        <p class="vc-page-subtitle">
            <spring:message code="contact.subtitle"/>
        </p>
    </div>

    <div class="vc-contact-content">

        <div class="vc-contact-intro">
            <h2>
                <spring:message code="contact.heading"/>
            </h2>

            <p>
                <spring:message code="contact.description"/>
            </p>
        </div>

        <div class="vc-contact-details">

            <div class="vc-contact-item">
                <div class="vc-contact-icon">
                    <i class="fas fa-map-marker-alt"></i>
                </div>

                <div>
                    <h3>
                        <spring:message code="contact.address"/>
                    </h3>

                    <p>
                        <spring:message code="contact.address.street"/>
                    </p>

                    <p>
                        <spring:message code="contact.address.postcode"/>
                    </p>
                </div>
            </div>

            <div class="vc-contact-item">
                <div class="vc-contact-icon">
                    <i class="fas fa-phone"></i>
                </div>

                <div>
                    <h3>
                        <spring:message code="contact.phone"/>
                    </h3>

                    <spring:message code="contact.phone.value" var="phoneValue"/>
                    <a href="tel:${phoneValue}">
                        <spring:message code="contact.phone.value"/>
                    </a>
                </div>
            </div>

            <div class="vc-contact-item">
                <div class="vc-contact-icon">
                    <i class="fas fa-envelope"></i>
                </div>

                <div>
                    <h3>
                        <spring:message code="contact.email"/>
                    </h3>

                    <spring:message code="contact.email.value" var="emailValue"/>
                    <a href="mailto:${emailValue}">
                        <spring:message code="contact.email.value"/>
                    </a>
                </div>
            </div>

        </div>

        <div class="vc-contact-socials">
            <a href="https://www.facebook.com/"
               target="_blank"
               rel="noopener noreferrer"
               aria-label="<spring:message code='contact.facebook'/>">
                <i class="fab fa-facebook-f"></i>
            </a>

            <a href="https://www.instagram.com/"
               target="_blank"
               rel="noopener noreferrer"
               aria-label="<spring:message code='contact.instagram'/>">
                <i class="fab fa-instagram"></i>
            </a>

            <a href="https://www.x.com/"
               target="_blank"
               rel="noopener noreferrer"
               aria-label="<spring:message code='contact.twitter'/>">
                <i class="fab fa-x-twitter"></i>
            </a>
        </div>

    </div>

</section>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
