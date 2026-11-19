
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>

<c:set var="logoutMessage"
       value="${sessionScope.vcLogoutMessage}"/>

<c:remove var="vcLogoutMessage" scope="session"/>

<div id="vc-notification-container">

    <c:if test="${not empty logoutMessage}">
        <div class="vc-alert vc-alert-success vc-alert-visible"
             role="status">

            <i class="fas fa-check-circle"></i>

            <span><c:out value="${logoutMessage}"/></span>

        </div>
    </c:if>

</div>
<script>
document.addEventListener("DOMContentLoaded", function () {
    const logoutAlert = document.querySelector(
        "#vc-notification-container .vc-alert"
    );

    if (!logoutAlert) {
        return;
    }

    setTimeout(function () {
        logoutAlert.classList.remove("vc-alert-visible");
        logoutAlert.classList.add("vc-alert-hiding");

        setTimeout(function () {
            logoutAlert.remove();
        }, 400);
    }, 4000);
});
</script>

<script>
document.addEventListener("DOMContentLoaded", function () {

    // ZAPISANIE POZYCJI PRZEWIJANIA PRZED PRZEJŚCIEM DO LOGOWANIA
    document.addEventListener("click", function (event) {

        const loginLink = event.target.closest(".vc-login-redirect");

        if (!loginLink) {
            return;
        }

        sessionStorage.setItem(
            "vc-home-scroll-position",
            window.scrollY
        );
    });

    // PRZYWRÓCENIE POZYCJI PO POWROCIE NA STRONĘ GŁÓWNĄ
    const savedPosition =
        sessionStorage.getItem("vc-home-scroll-position");

    if (savedPosition !== null) {

        sessionStorage.removeItem("vc-home-scroll-position");

        window.addEventListener("load", function () {

            window.scrollTo({
                top: Number(savedPosition),
                behavior: "instant"
            });

        });

    }

});
</script>

<section class="vc-albums-section">

    <div class="vc-section-heading">

        <div>
            <span class="vc-eyebrow">DISCOGS</span>

            <h2>
                <spring:message code="home.freshAlbums"/>
            </h2>
        </div>

    </div>

    <div class="vc-album-grid">

        <c:choose>

            <c:when test="${not empty thumbs}">

                <c:forEach items="${thumbs}" var="e">

                    <article class="vc-album-card">

                        <a class="vc-album-image"
                           href="${pageContext.request.contextPath}/home/details/${e.id}">

                            <c:choose>

                                <c:when test="${not empty e.image}">

                                    <img src="${e.image}"
                                         alt="${e.title}"
                                         loading="lazy"/>

                                </c:when>

                                <c:otherwise>

                                    <div class="vc-image-placeholder">
                                        <i class="fas fa-compact-disc"></i>
                                    </div>

                                </c:otherwise>

                            </c:choose>

                            <span class="vc-image-overlay">
                                <spring:message code="album.details"/>
                                <i class="fas fa-arrow-right"></i>
                            </span>

                        </a>

                        <div class="vc-album-info">

                            <c:if test="${not empty e.artist}">
                                <p class="vc-album-artist">
                                    <c:out value="${e.artist}"/>
                                </p>
                            </c:if>

                            <h3 class="vc-album-title">
                                <c:out value="${e.title}"/>
                            </h3>

                            <sec:authorize access="isAuthenticated()">

                                <form action="${pageContext.request.contextPath}/home/add/${e.id}"
                                      method="post"
                                      class="vc-add-form">

                                    <input type="hidden"
                                           name="${_csrf.parameterName}"
                                           value="${_csrf.token}"/>

                                    <button type="submit" class="vc-add-button">
                                        <i class="fas fa-plus"></i>
                                        <spring:message code="album.add"/>
                                    </button>

                                </form>

                            </sec:authorize>

                            <sec:authorize access="isAnonymous()">

                                <a href="<c:url value='/login'>
                                             <c:param name='returnUrl' value='/home/start'/>
                                             <c:param name='addAlbumId' value='${e.id}'/>
                                         </c:url>"
                                   class="vc-add-button vc-login-redirect">

                                    <i class="fas fa-plus"></i>
                                    <spring:message code="album.add"/>

                                </a>

                            </sec:authorize>

                        </div>

                    </article>

                </c:forEach>

            </c:when>

            <c:otherwise>

                <div class="vc-empty-state">

                    <i class="fas fa-compact-disc"></i>

                    <p>
                        <spring:message code="common.noAlbums"/>
                    </p>

                </div>

            </c:otherwise>

        </c:choose>

    </div>

    <div class="vc-pagination-slot"></div>

</section>

<script src="<c:url value='/resources/html-css/js/home.js'/>"></script>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
