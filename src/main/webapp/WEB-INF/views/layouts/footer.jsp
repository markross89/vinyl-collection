
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

    </main>

    <!-- Footer -->
    <footer class="vc-footer">

        <div class="vc-footer-brand">
            <i class="fas fa-compact-disc"></i>
            <span><spring:message code="app.name"/></span>
        </div>

        <p>
            <spring:message code="footer.copyright"/>
        </p>

        <div class="vc-footer-links">

            <a href="${pageContext.request.contextPath}/about">
                <spring:message code="nav.about"/>
            </a>

            <a href="${pageContext.request.contextPath}/contact">
                <spring:message code="nav.contact"/>
            </a>

        </div>

    </footer>

</div>

<!-- GLOBAL NOTIFICATION CONTAINER -->
<div id="vc-notification-container"></div>

<!-- GLOBAL CREATE MODAL -->
<c:if test="${not empty requestScope.createModalUrl}">
    <jsp:include page="/WEB-INF/views/layouts/create-modal.jsp"/>
</c:if>

<!-- GLOBAL DELETE MODAL -->
<jsp:include page="/WEB-INF/views/layouts/delete-modal.jsp"/>

<c:if test="${not empty notificationMessage}">
    <spring:message code="${notificationMessage}" var="flashMessage"/>

    <div id="vc-flash-notification"
         data-message="${fn:escapeXml(flashMessage)}"
         data-type="${notificationType}">
    </div>
</c:if>

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/resources/html-css/vendor/jquery/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/js/sb-admin-2.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/html-css/js/notifications.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/js/scroll-position.js"></script>

<script src="${pageContext.request.contextPath}/resources/html-css/js/common/create-add-modal.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/js/common/create-modal.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/js/common/delete-modal.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/js/tracklist-reorder.js"></script>
<script src="${pageContext.request.contextPath}/resources/html-css/js/tracklist-playback.js"></script>

</body>
</html>
