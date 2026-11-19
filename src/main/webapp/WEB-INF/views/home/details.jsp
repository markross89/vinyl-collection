
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>

<main class="vc-details-page">
    <div class="vc-details-container">

        <!-- Back -->
        <!-- Back -->
        <a href="<c:url value='/home/start'/>" class="vc-back-link">
            <i class="fas fa-arrow-left"></i>
            <spring:message code="common.back" text="Back"/>
        </a>

        <!-- Album Heading -->
        <div class="vc-details-heading">

            <span class="vc-eyebrow">Discogs release</span>

            <h1 class="vc-album-title">
                <c:out value="${albumDetails.title}"/>
            </h1>

            <p class="vc-album-artist">
                <c:out value="${albumDetails.artist}"/>
            </p>

        </div>

        <!-- Album Hero -->
        <section class="vc-album-hero">

            <!-- Cover -->
            <div class="vc-album-cover-wrapper">

                <c:choose>
                    <c:when test="${not empty images}">
                        <button type="button"
                                class="vc-album-cover-button"
                                id="vc-open-gallery"
                                aria-label="Open album gallery">

                            <img src="<c:out value='${images[0].uri}'/>"
                                 alt="<c:out value='${albumDetails.title}'/>"
                                 class="vc-album-cover"/>

                            <span class="vc-cover-overlay">
                                <i class="fas fa-search-plus"></i>
                            </span>
                        </button>

                        <c:if test="${fn:length(images) gt 1}">
                            <span class="vc-cover-image-count">
                                <i class="fas fa-images"></i>
                                <c:out value="${fn:length(images)}"/>
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

            <!-- Album Information -->
            <div class="vc-album-info">



                <c:if test="${not empty albumDetails.uri}">
                    <div class="vc-album-source">
                        <a href="<c:out value='${albumDetails.uri}'/>"
                           target="_blank"
                           rel="noopener noreferrer"
                           class="vc-action-button vc-action-discogs">

                            <i class="fas fa-external-link-alt"></i>
                            <spring:message code="album.details.discogs"/>
                        </a>
                    </div>
                </c:if>

                <!-- Metadata -->
                <div class="vc-album-meta">

                    <c:if test="${not empty albumDetails.label}">
                        <div class="vc-meta-item">
                            <span class="vc-meta-label">Label</span>
                            <span class="vc-meta-value">
                                <c:out value="${albumDetails.label}"/>
                            </span>
                        </div>
                    </c:if>

                    <c:if test="${not empty albumDetails.catno}">
                        <div class="vc-meta-item">
                            <span class="vc-meta-label">Catalog number</span>
                            <span class="vc-meta-value">
                                <c:out value="${albumDetails.catno}"/>
                            </span>
                        </div>
                    </c:if>

                    <c:if test="${not empty albumDetails.genre}">
                        <div class="vc-meta-item">
                            <span class="vc-meta-label">Genre</span>
                            <span class="vc-meta-value">
                                <c:out value="${albumDetails.genre}"/>
                            </span>
                        </div>
                    </c:if>

                    <div class="vc-meta-item">
                        <span class="vc-meta-label">Discogs ID</span>
                        <span class="vc-meta-value">
                            <c:out value="${albumDetails.id}"/>
                        </span>
                    </div>

                </div>

                <!-- Actions -->
               <div class="vc-album-actions">

                   <sec:authorize access="isAuthenticated()">
                       <form action="<c:url value='/home/add/${albumDetails.id}'/>"
                             method="post"
                             class="vc-add-form">

                           <input type="hidden"
                                  name="${_csrf.parameterName}"
                                  value="${_csrf.token}"/>

                           <button type="submit" class="vc-action-button vc-action-add">
                               <i class="fas fa-plus"></i>
                               <spring:message code="album.addToCollection"/>
                           </button>
                       </form>
                   </sec:authorize>

                   <sec:authorize access="isAnonymous()">

                       <a href="<c:url value='/login'>
                                    <c:param name='returnUrl'
                                             value='/home/details/${albumDetails.id}'/>
                                    <c:param name='addAlbumId'
                                             value='${albumDetails.id}'/>
                                </c:url>"
                          class="vc-action-button vc-action-add">

                           <i class="fas fa-plus"></i>
                           <spring:message code="album.addToCollection"/>

                       </a>

                   </sec:authorize>

               </div>

            </div>
        </section>

        <!-- Tracklist -->
        <section class="vc-tracklist-section">

            <div class="vc-section-heading">
                <div>
                    <span class="vc-eyebrow">Album content</span>
                    <h2>Tracklist</h2>
                </div>

                <span class="vc-track-count">
                    <i class="fas fa-music"></i>
                    <c:out value="${fn:length(tracklist)}"/> tracks
                </span>
            </div>

            <c:choose>
                <c:when test="${not empty tracklist}">
                    <div class="vc-tracklist">

                        <c:forEach items="${tracklist}" var="track">
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

<!-- Gallery Modal -->
<c:if test="${not empty images}">
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
                     src="<c:out value='${images[0].uri}'/>"
                     alt="Album image"/>

                <button type="button"
                        class="vc-gallery-nav vc-gallery-next"
                        id="vc-gallery-next"
                        aria-label="Next image">
                    <i class="fas fa-chevron-right"></i>
                </button>

            </div>

            <div class="vc-gallery-counter" id="vc-gallery-counter">
                1 / <c:out value="${fn:length(images)}"/>
            </div>

            <div class="vc-gallery-thumbnails" id="vc-gallery-thumbnails">

                <c:forEach items="${images}" var="image" varStatus="status">
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

<script src="<c:url value='/resources/html-css/js/home.js'/>"></script>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>

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
            counter.textContent = (currentIndex + 1) + " / " + images.length;

            images.forEach(function (thumbnail, i) {
                thumbnail.classList.toggle("active", i === currentIndex);
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

        openButton.addEventListener("click", openGallery);
        closeButton.addEventListener("click", closeGallery);

        previousButton.addEventListener("click", function () {
            showImage(currentIndex - 1);
        });

        nextButton.addEventListener("click", function () {
            showImage(currentIndex + 1);
        });

        thumbnails.addEventListener("click", function (event) {
            const thumbnail = event.target.closest(".vc-gallery-thumbnail");

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
