
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>
<jsp:include page="/WEB-INF/views/album/album-i18n.jsp"/>

<section class="vc-albums-section">

    <div class="vc-section-heading">
        <div>
            <span class="vc-eyebrow">VINYL COLLECTION</span>
            <h2>
                <spring:message code="album.title"/>
            </h2>
            <p class="vc-page-description">
                <spring:message code="album.collection.description"/>
            </p>
        </div>
    </div>

    <div class="vc-album-grid">

        <c:choose>
            <c:when test="${not empty albums}">

                <c:forEach items="${albums}" var="album">

                    <article class="vc-album-card">

                        <a class="vc-album-image"
                           href="${pageContext.request.contextPath}/album/details/${album.discogsId}">

                            <c:set var="coverImage" value=""/>
                            <c:set var="firstImage" value=""/>

                            <c:forEach items="${album.images}" var="image">

                                <c:if test="${empty firstImage}">
                                    <c:set var="firstImage" value="${image.uri}"/>
                                </c:if>

                                <c:if test="${image.type eq 'primary' and empty coverImage}">
                                    <c:set var="coverImage" value="${image.uri}"/>
                                </c:if>

                            </c:forEach>

                            <c:if test="${empty coverImage}">
                                <c:set var="coverImage" value="${firstImage}"/>
                            </c:if>

                            <c:choose>
                                <c:when test="${not empty coverImage}">
                                    <img src="${coverImage}"
                                         alt="${fn:escapeXml(album.artist)} - ${fn:escapeXml(album.title)}"
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

                            <p class="vc-album-artist">
                                <c:out value="${album.artist}"/>
                            </p>

                            <h3 class="vc-album-title">
                                <c:out value="${album.title}"/>
                            </h3>

                            <div class="vc-album-actions">

                                <button type="button"
                                        class="vc-add-button js-open-box-modal"
                                        data-album-id="${album.discogsId}"
                                        data-album-title="${fn:escapeXml(album.artist)} - ${fn:escapeXml(album.title)}">

                                    <i class="fas fa-plus"></i>
                                    <spring:message code="album.addToBox"/>

                                </button>

                                <button type="button"
                                        class="vc-remove-button js-open-delete-modal"
                                        data-delete-url="${pageContext.request.contextPath}/album/api/${album.discogsId}"
                                        data-delete-name="${fn:escapeXml(album.artist)} - ${fn:escapeXml(album.title)}">

                                    <i class="fas fa-trash-alt"></i>
                                    <spring:message code="album.remove"/>

                                </button>

                            </div>

                        </div>

                    </article>

                </c:forEach>

            </c:when>

            <c:otherwise>

                <div class="vc-empty-state">
                    <i class="fas fa-compact-disc"></i>
                    <p>
                        <spring:message code="album.empty.title"/>
                    </p>
                </div>

            </c:otherwise>
        </c:choose>

    </div>

</section>


<jsp:include page="/WEB-INF/views/layouts/create-add-modal.jsp">

    <jsp:param name="eyebrowCode" value="album.eyebrow"/>
    <jsp:param name="titleCode" value="box.modal.title"/>
    <jsp:param name="createHeadingCode" value="box.create.heading"/>
    <jsp:param name="placeholderCode" value="box.create.placeholder"/>
    <jsp:param name="confirmCode" value="box.create.button"/>

</jsp:include>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
