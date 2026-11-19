
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/box/box-i18n.jsp"/>
<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>

<section class="vc-albums-section">

    <div class="vc-section-heading">

        <div>
            <span class="vc-eyebrow">
                <spring:message code="box.eyebrow"/>
            </span>

            <h2>
                <spring:message code="box.myBoxes"/>
            </h2>

            <p class="vc-section-description">
                <spring:message code="box.description"/>
            </p>
        </div>

        <button type="button"
                class="vc-add-button vc-create-box-button"
                id="vc-open-create-box"
                data-open-create-modal>

            <i class="fas fa-plus"></i>
            <spring:message code="box.create"/>

        </button>

    </div>

    <div class="vc-album-grid">

        <c:choose>

            <c:when test="${not empty boxes}">

                <c:forEach items="${boxes}" var="box">

                    <article class="vc-album-card vc-box-card">

                        <a class="vc-album-image vc-box-image"
                           href="${pageContext.request.contextPath}/box/details/${box.id}">

                            <img src="${pageContext.request.contextPath}/resources/html-css/images/vinyl-box.jpg"
                                 alt="Vinyl records in a wooden storage box"
                                 loading="lazy"/>

                            <span class="vc-image-overlay">
                                <spring:message code="box.open"/>
                                <i class="fas fa-arrow-right"></i>
                            </span>

                        </a>

                        <div class="vc-album-info">

                            <h3 class="vc-album-title vc-box-title">
                                <c:out value="${box.name}"/>
                            </h3>

                            <p class="vc-box-date">
                                <i class="far fa-calendar-alt"></i>
                                <c:out value="${box.date}"/>
                            </p>

                            <p class="vc-box-count">
                                <i class="fas fa-compact-disc"></i>

                                <spring:message code="box.albumCount"
                                                arguments="${box.albums.size()}"/>
                            </p>

                            <div class="vc-album-actions">

                                <button type="button"
                                        class="vc-remove-button js-open-delete-modal"
                                        data-delete-url="${pageContext.request.contextPath}/box/api/${box.id}"
                                        data-delete-name="${fn:escapeXml(box.name)}">

                                    <i class="fas fa-trash-alt"></i>
                                    <spring:message code="box.delete"/>

                                </button>

                            </div>

                        </div>

                    </article>

                </c:forEach>

            </c:when>

            <c:otherwise>

                <div class="vc-empty-state">

                    <i class="fas fa-box-open"></i>

                    <h2>
                        <spring:message code="box.empty.title"/>
                    </h2>

                    <p>
                        <spring:message code="box.empty.description"/>
                    </p>

                </div>

            </c:otherwise>

        </c:choose>

    </div>

</section>

<!-- CREATE MODAL CONFIGURATION -->

<c:set var="createModalUrl"
       value="${pageContext.request.contextPath}/box/api/create-empty"
       scope="request"/>

<c:set var="createModalTitleCode"
       value="box.createModal.title"
       scope="request"/>

<c:set var="createModalDescriptionCode"
       value="box.createModal.description"
       scope="request"/>

<c:set var="createModalPlaceholderCode"
       value="box.createModal.placeholder"
       scope="request"/>

<c:set var="createModalConfirmCode"
       value="box.createModal.confirm"
       scope="request"/>

<c:set var="createModalErrorCode"
       value="box.create.error"
       scope="request"/>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
