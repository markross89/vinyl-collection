
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>
<jsp:include page="/WEB-INF/views/box/box-i18n.jsp"/>


<section class="vc-albums-section">

<a href="${pageContext.request.contextPath}/box/boxes">
    <i class="fas fa-arrow-left"></i>
    <spring:message code="common.back"/>
</a>

    <!-- BOX HEADER -->
    <div class="vc-section-heading vc-box-details-heading">

        <div class="vc-box-heading-info">

            <span class="vc-eyebrow">
                <spring:message code="box.details.eyebrow"/>
            </span>

            <h2 class="vc-box-main-title">
                <c:out value="${box.name}"/>
            </h2>

            <div class="vc-box-meta">

                <span>
                    <i class="far fa-calendar-alt"></i>
                    <c:out value="${box.date}"/>
                </span>

                <span>
                    <i class="fas fa-compact-disc"></i>
                    <spring:message code="box.albumCount"
                                    arguments="${fn:length(thumbs)}"/>
                </span>

            </div>

        </div>

        <div class="vc-box-heading-actions">



            <button type="button"
                    class="vc-remove-button js-open-delete-modal"
                    data-delete-url="${pageContext.request.contextPath}/box/api/${box.id}"
                    data-delete-name="${fn:escapeXml(box.name)}"
                    data-delete-redirect="${pageContext.request.contextPath}/box/boxes">

                <i class="fas fa-trash-alt"></i>
                <spring:message code="box.delete"/>

            </button>

        </div>

    </div>

    <!-- ALBUM GRID -->
    <div class="vc-album-grid">

        <c:choose>

            <c:when test="${not empty thumbs}">

                <c:forEach items="${thumbs}" var="e">

                    <article class="vc-album-card">

                       <a class="vc-album-image"
                          href="<c:url value='/album/details/${e.id}'>
                                   <c:param name='returnUrl' value='/box/details/${box.id}'/>
                               </c:url>">

                            <c:choose>

                                <c:when test="${not empty e.image}">
                                    <img src="${e.image}"
                                         alt="${fn:escapeXml(e.title)}"
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

                            <div class="vc-album-actions">

                                <button type="button"
                                        class="vc-remove-button js-open-delete-modal"
                                        data-delete-url="${pageContext.request.contextPath}/box/api/${box.id}/albums/${e.id}"
                                        data-delete-name="${fn:escapeXml(e.artist)} - ${fn:escapeXml(e.title)}">

                                    <i class="fas fa-trash-alt"></i>
                                    <spring:message code="box.removeAlbum"/>

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
                        <spring:message code="box.details.empty.title"/>
                    </h2>

                    <p>
                        <spring:message code="box.details.empty.description"/>
                    </p>

                    <a href="${pageContext.request.contextPath}/album/albums"
                       class="vc-add-button">
                        <i class="fas fa-plus"></i>
                        <spring:message code="box.details.browseAlbums"/>
                    </a>

                </div>

            </c:otherwise>

        </c:choose>

    </div>

</section>


<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
