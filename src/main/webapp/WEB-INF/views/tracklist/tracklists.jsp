
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/tracklist/tracklist-i18n.jsp"/>
<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>

<section class="vc-albums-section">

    <div class="vc-section-heading">

        <div>
            <span class="vc-eyebrow">
                <spring:message code="tracklists.eyebrow"/>
            </span>

            <h2>
                <spring:message code="tracklists.title"/>
            </h2>

            <p class="vc-section-description">
                <spring:message code="tracklists.description"/>
            </p>
        </div>

        <button type="button"
                class="vc-add-button vc-create-box-button"
                id="vc-open-create-tracklist"
                data-open-create-modal>

            <i class="fas fa-plus"></i>
            <spring:message code="tracklists.create"/>

        </button>

    </div>

    <div class="vc-album-grid">

        <c:choose>

            <c:when test="${not empty tracklists}">

                <c:forEach items="${tracklists}" var="tracklist">

                    <article class="vc-album-card vc-tracklist-card">

                        <a class="vc-album-image vc-tracklist-image"
                           href="${pageContext.request.contextPath}/tracklist/${tracklist.id}">

                            <img src="${pageContext.request.contextPath}/resources/html-css/images/playlist.png"
                                 alt="Playlist"
                                 loading="lazy"/>

                            <span class="vc-image-overlay">
                                <spring:message code="tracklists.open"/>
                                <i class="fas fa-arrow-right"></i>
                            </span>

                        </a>

                        <div class="vc-album-info">

                            <h3 class="vc-album-title">
                                <c:out value="${tracklist.name}"/>
                            </h3>

                            <p class="vc-box-date">
                                <i class="far fa-calendar-alt"></i>
                                <c:out value="${tracklist.date}"/>
                            </p>

                            <p class="vc-box-count">
                                <i class="fas fa-music"></i>

                                <spring:message code="tracklists.trackCount"
                                                arguments="${tracklist.tracks.size()}"/>
                            </p>

                            <div class="vc-album-actions">

                                <button type="button"
                                        class="vc-add-button"
                                        data-open-create-modal
                                        data-create-url="${pageContext.request.contextPath}/tracklist/api/${tracklist.id}/create-box"
                                        data-create-title="<spring:message code='tracklist.createBox.modal.title'/>"
                                        data-create-description="<spring:message code='tracklist.createBox.modal.description'/>"
                                        data-create-placeholder="<spring:message code='tracklist.createBox.modal.placeholder'/>"
                                        data-create-confirm="<spring:message code='tracklist.createBox.modal.confirm'/>"
                                        data-create-error="<spring:message code='tracklist.createBox.error'/>">

                                    <i class="fas fa-plus"></i>
                                    <spring:message code="tracklists.createBox"/>

                                </button>

                                <button type="button"
                                        class="vc-remove-button js-open-delete-modal"
                                        data-delete-url="${pageContext.request.contextPath}/tracklist/api/${tracklist.id}"
                                        data-delete-name="${fn:escapeXml(tracklist.name)}">

                                    <i class="fas fa-trash-alt"></i>
                                    <spring:message code="tracklists.delete"/>

                                </button>

                            </div>

                        </div>

                    </article>

                </c:forEach>

            </c:when>

            <c:otherwise>

                <div class="vc-empty-state">

                    <i class="fas fa-list"></i>

                    <h2>
                        <spring:message code="tracklists.empty.title"/>
                    </h2>

                    <p>
                        <spring:message code="tracklists.empty.description"/>
                    </p>

                    <a href="${pageContext.request.contextPath}/track/tracks"
                       class="vc-remove-button">

                        <i class="fas fa-music"></i>
                        <spring:message code="tracklists.empty.button"/>

                    </a>

                </div>

            </c:otherwise>

        </c:choose>

    </div>

</section>

<!-- CREATE PLAYLIST MODAL CONFIGURATION -->

<c:set var="createModalUrl"
       value="${pageContext.request.contextPath}/tracklist/api"
       scope="request"/>

<c:set var="createModalTitleCode"
       value="tracklists.createModal.title"
       scope="request"/>

<c:set var="createModalDescriptionCode"
       value="tracklists.createModal.description"
       scope="request"/>

<c:set var="createModalPlaceholderCode"
       value="tracklists.createModal.placeholder"
       scope="request"/>

<c:set var="createModalConfirmCode"
       value="tracklists.createModal.confirm"
       scope="request"/>

<c:set var="createModalErrorCode"
       value="tracklist.create.error"
       scope="request"/>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>

