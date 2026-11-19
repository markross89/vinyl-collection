
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>
<jsp:include page="/WEB-INF/views/album/album-i18n.jsp"/>

<main class="vc-details-page">
    <div class="vc-details-container">

        <%-- Back --%>
        <a href="${pageContext.request.contextPath}${backUrl}"
           class="vc-back-link">

            <i class="fas fa-arrow-left"></i>
            <spring:message code="common.back"/>

        </a>
        <%-- Album Heading --%>
        <div class="vc-album-heading">

            <span class="vc-eyebrow">Discogs release</span>

            <h1 class="vc-album-title">
                <c:out value="${album.title}"/>
            </h1>

            <p class="vc-album-artist">
                <c:out value="${album.artist}"/>
            </p>

        </div>

        <%-- Cover image --%>
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
        <%-- Album Hero --%>
        <section class="vc-album-hero">

            <div class="vc-album-cover-wrapper">

                <c:choose>

                    <c:when test="${not empty coverImage}">

                        <button type="button"
                                class="vc-album-cover-button"
                                id="vc-open-gallery"
                                aria-label="Open album gallery">

                            <img src="<c:out value='${coverImage}'/>"
                                 alt="<c:out value='${album.title}'/>"
                                 class="vc-album-cover"/>

                            <span class="vc-cover-overlay">
                                <i class="fas fa-search-plus"></i>
                            </span>

                        </button>

                        <c:if test="${fn:length(album.images) gt 1}">
                            <span class="vc-cover-image-count">
                                <i class="fas fa-images"></i>
                                <c:out value="${fn:length(album.images)}"/>
                            </span>
                        </c:if>

                    </c:when>

                    <c:otherwise>

                        <div class="vc-album-cover-placeholder">
                            <i class="fas fa-record-vinyl"></i>
                        </div>

                    </c:otherwise>

                </c:choose>

            </div>

            <%-- Album Information --%>
            <div class="vc-album-info">

                <%-- Discogs source --%>
                <c:if test="${not empty album.uri}">

                    <div class="vc-album-source">

                        <a href="<c:out value='${album.uri}'/>"
                           target="_blank"
                           rel="noopener noreferrer"
                           class="vc-action-button vc-action-discogs">

                            <i class="fas fa-external-link-alt"></i>
                            <spring:message code="album.details.discogs"/>

                        </a>

                    </div>

                </c:if>

                <%-- Metadata --%>
                <div class="vc-album-meta">

                    <c:if test="${not empty album.label}">
                        <div class="vc-meta-item">

                            <span class="vc-meta-label">
                                <spring:message code="album.label"/>
                            </span>

                            <span class="vc-meta-value">
                                <c:out value="${album.label}"/>
                            </span>

                        </div>
                    </c:if>

                    <c:if test="${not empty album.catno}">
                        <div class="vc-meta-item">

                            <span class="vc-meta-label">
                                <spring:message code="album.catno"/>
                            </span>

                            <span class="vc-meta-value">
                                <c:out value="${album.catno}"/>
                            </span>

                        </div>
                    </c:if>

                    <c:if test="${not empty album.genre}">
                        <div class="vc-meta-item">

                            <span class="vc-meta-label">
                                <spring:message code="album.genre"/>
                            </span>

                            <span class="vc-meta-value">
                                <c:out value="${album.genre}"/>
                            </span>

                        </div>
                    </c:if>

                    <c:if test="${not empty album.date}">
                        <div class="vc-meta-item">

                            <span class="vc-meta-label">
                                <spring:message code="album.year"/>
                            </span>

                            <span class="vc-meta-value">
                                <c:out value="${album.date.year}"/>
                            </span>

                        </div>
                    </c:if>

                    <div class="vc-meta-item">

                        <span class="vc-meta-label">Discogs ID</span>

                        <span class="vc-meta-value">
                            <c:out value="${album.discogsId}"/>
                        </span>

                    </div>

                </div>

                <%-- Collection Actions --%>
                <div class="vc-album-actions">

                   <button type="button"
                           id="vc-details-remove-button"
                           class="vc-action-button vc-action-remove js-open-delete-modal"
                           data-delete-url="${pageContext.request.contextPath}/album/api/${album.discogsId}"
                           data-delete-name="${fn:escapeXml(album.artist)} - ${fn:escapeXml(album.title)}"
                           data-delete-redirect="${pageContext.request.contextPath}${backUrl}"
                           <c:if test="${not isInCollection}">disabled</c:if>>

                        <i class="fas fa-trash-alt"></i>

                        <spring:message code="album.remove"/>

                    </button>

                    <button type="button"
                            id="vc-details-box-button"
                            class="vc-action-button vc-action-add js-open-box-modal"
                            data-album-id="${album.discogsId}"
                            data-album-title="${fn:escapeXml(album.artist)} - ${fn:escapeXml(album.title)}"
                            <c:if test="${not isInCollection}">disabled</c:if>>

                        <i class="fas fa-box"></i>

                        <spring:message code="album.addToBox"/>

                    </button>

                </div>
            </div>

        </section>

        <%-- Tracklist --%>
        <section class="vc-tracklist-section">

            <div class="vc-section-heading">

                <div>
                    <span class="vc-eyebrow">Album content</span>
                    <h2>Tracklist</h2>
                </div>

                <span class="vc-track-count">
                    <i class="fas fa-music"></i>
                    <c:out value="${fn:length(album.tracks)}"/> tracks
                </span>

            </div>

            <c:choose>

                <c:when test="${not empty album.tracks}">

                    <div class="vc-tracklist">

                        <c:forEach items="${album.tracks}" var="track">

                            <div class="vc-track-row">

                                <span class="vc-track-position">
                                    <c:out value="${track.position}"/>
                                </span>

                                <span class="vc-track-title">
                                    <c:out value="${track.title}"/>
                                </span>

                                <span class="vc-track-duration">
                                    <c:out value="${track.duration}"/>
                                </span>

                            </div>

                        </c:forEach>

                    </div>

                </c:when>

                <c:otherwise>

                    <div class="vc-empty-tracklist">

                        <i class="fas fa-music"></i>

                        <p>No tracklist available.</p>

                    </div>

                </c:otherwise>

            </c:choose>

        </section>

    </div>
</main>

<%-- Gallery Modal --%>
<c:if test="${not empty album.images}">

    <div class="vc-gallery-modal" id="vc-gallery-modal">

        <div class="vc-gallery-backdrop"></div>

        <div class="vc-gallery-dialog">

            <div class="vc-gallery-header">

                <span>Album gallery</span>

                <button type="button"
                        class="vc-gallery-close"
                        id="vc-gallery-close"
                        aria-label="Close gallery">

                    <i class="fas fa-times"></i>

                </button>

            </div>

            <div class="vc-gallery-main">

                <button type="button"
                        class="vc-gallery-nav vc-gallery-prev"
                        id="vc-gallery-prev"
                        aria-label="Previous image">

                    <i class="fas fa-chevron-left"></i>

                </button>

                <img id="vc-gallery-main-image"
                     src="<c:out value='${album.images[0].uri}'/>"
                     alt="Album image"/>

                <button type="button"
                        class="vc-gallery-nav vc-gallery-next"
                        id="vc-gallery-next"
                        aria-label="Next image">

                    <i class="fas fa-chevron-right"></i>

                </button>

            </div>

            <div class="vc-gallery-counter" id="vc-gallery-counter">
                1 / <c:out value="${fn:length(album.images)}"/>
            </div>

            <div class="vc-gallery-thumbnails" id="vc-gallery-thumbnails">

                <c:forEach items="${album.images}" var="image" varStatus="status">

                    <button type="button"
                            class="vc-gallery-thumbnail ${status.first ? 'active' : ''}"
                            data-index="${status.index}"
                            data-image="<c:out value='${image.uri}'/>">

                        <img src="<c:out value='${image.uri}'/>"
                             alt="<c:out value='${image.type}'/>"/>

                    </button>

                </c:forEach>

            </div>

        </div>

    </div>

</c:if>

<%-- Add to Box Modal --%>
<jsp:include page="/WEB-INF/views/layouts/create-add-modal.jsp">

    <jsp:param name="eyebrowCode" value="album.eyebrow"/>
    <jsp:param name="titleCode" value="box.modal.title"/>
    <jsp:param name="createHeadingCode" value="box.create.heading"/>
    <jsp:param name="placeholderCode" value="box.create.placeholder"/>
    <jsp:param name="confirmCode" value="box.create.button"/>

</jsp:include>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>

<%-- Gallery JavaScript --%>
<script>
    document.addEventListener("DOMContentLoaded", function () {

        const modal = document.getElementById("vc-gallery-modal");

        if (!modal) {
            return;
        }

        const images = Array.from(
            modal.querySelectorAll(".vc-gallery-thumbnail")
        );

        const mainImage = document.getElementById("vc-gallery-main-image");
        const counter = document.getElementById("vc-gallery-counter");
        const thumbnails = document.getElementById("vc-gallery-thumbnails");

        const openButton = document.getElementById("vc-open-gallery");
        const closeButton = document.getElementById("vc-gallery-close");
        const previousButton = document.getElementById("vc-gallery-prev");
        const nextButton = document.getElementById("vc-gallery-next");

        let currentIndex = 0;

        function showImage(index) {

            if (images.length === 0) {
                return;
            }

            currentIndex = (index + images.length) % images.length;

            const selected = images[currentIndex];

            mainImage.src = selected.dataset.image;

            counter.textContent =
                (currentIndex + 1) + " / " + images.length;

            images.forEach(function (thumbnail, i) {

                thumbnail.classList.toggle(
                    "active",
                    i === currentIndex
                );

            });

        }

        function openGallery() {

            modal.classList.add("active");
            document.body.style.overflow = "hidden";

            showImage(0);

        }

        function closeGallery() {

            modal.classList.remove("active");
            document.body.style.overflow = "";

        }

        if (openButton) {
            openButton.addEventListener("click", openGallery);
        }

        closeButton.addEventListener("click", closeGallery);

        previousButton.addEventListener("click", function () {

            showImage(currentIndex - 1);

        });

        nextButton.addEventListener("click", function () {

            showImage(currentIndex + 1);

        });

        thumbnails.addEventListener("click", function (event) {

            const thumbnail = event.target.closest(
                ".vc-gallery-thumbnail"
            );

            if (!thumbnail) {
                return;
            }

            showImage(Number(thumbnail.dataset.index));

        });

        modal.querySelector(".vc-gallery-backdrop")
            .addEventListener("click", closeGallery);

        document.addEventListener("keydown", function (event) {

            if (!modal.classList.contains("active")) {
                return;
            }

            if (event.key === "Escape") {

                closeGallery();

            } else if (event.key === "ArrowLeft") {

                showImage(currentIndex - 1);

            } else if (event.key === "ArrowRight") {

                showImage(currentIndex + 1);

            }

        });

    });
</script>
