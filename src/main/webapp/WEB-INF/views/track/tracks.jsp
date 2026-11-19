
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>
<jsp:include page="/WEB-INF/views/track/track-i18n.jsp"/>

<main class="vc-main vc-songs-section vc-tracks-page">
<div class="vc-section-heading">
    <div>
        <span class="vc-eyebrow">
            <spring:message code="songs.eyebrow"/>
        </span>
        <h2>
            <spring:message code="songs.myTracks"/>
        </h2>
        <p class="vc-section-description">
            <spring:message code="songs.description"/>
        </p>
    </div>
</div>

    <%-- SONGS OVERVIEW --%>
    <div class="vc-songs-overview">

        <%-- LEFT PANEL: SEARCH AND TRACK COUNT --%>
        <div class="vc-songs-sidebar">

            <form class="vc-search"
                      action="<c:url value='/track/searchCollection'/>"
                      method="get">

                    <i class="fas fa-search"></i>

                    <input type="text"
                           name="query"
                           value="${fn:escapeXml(param.query)}"
                           placeholder="<spring:message code='songs.search.placeholder'/>"
                           aria-label="<spring:message code='songs.search.placeholder'/>"
                           required/>

                    <button type="submit" aria-label="Search">
                        <i class="fas fa-arrow-right"></i>
                    </button>

                </form>

            <div class="vc-songs-count">
                <c:choose>
                    <c:when test="${not empty tracks}">
                        ${fn:length(tracks)}
                        <spring:message code="songs.trackCount"/>
                    </c:when>
                    <c:otherwise>
                        <spring:message code="songs.yourTracks"/>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>

        <%-- RIGHT PANEL: SORTING AND GENRE FILTER --%>
        <div class="vc-songs-main-controls">

            <div class="vc-songs-controls">

                <%-- SORT BY --%>
                <div class="vc-songs-control-group">

                    <span class="vc-songs-control-label">
                        <spring:message code="songs.sortBy"/>
                    </span>

                    <div class="vc-songs-sort">

                        <a href="<c:url value='/track/sortBy/artist'/>"
                           class="vc-songs-sort-option ${activeSort eq 'artist' ? 'active' : ''}">
                            <spring:message code="songs.sort.artist"/>
                        </a>

                        <a href="<c:url value='/track/sortBy/title'/>"
                           class="vc-songs-sort-option ${activeSort eq 'title' ? 'active' : ''}">
                            <spring:message code="songs.sort.title"/>
                        </a>

                        <a href="<c:url value='/track/sortBy/album'/>"
                           class="vc-songs-sort-option ${activeSort eq 'album' ? 'active' : ''}">
                            <spring:message code="songs.sort.album"/>
                        </a>

                        <a href="<c:url value='/track/sortBy/duration'/>"
                           class="vc-songs-sort-option ${activeSort eq 'duration' ? 'active' : ''}">
                            <spring:message code="songs.sort.duration"/>
                        </a>

                        <a href="<c:url value='/track/sortBy/label'/>"
                           class="vc-songs-sort-option ${activeSort eq 'label' ? 'active' : ''}">
                            <spring:message code="songs.sort.label"/>
                        </a>

                        <a href="<c:url value='/track/sortBy/date'/>"
                           class="vc-songs-sort-option ${activeSort eq 'date' ? 'active' : ''}">
                            <spring:message code="songs.sort.date"/>
                        </a>

                    </div>
                </div>

                <%-- FILTER BY GENRE --%>
                <div class="vc-songs-control-group">

                    <span class="vc-songs-control-label">
                        <spring:message code="songs.filterByGenre"/>
                    </span>

                    <div class="vc-songs-sort">

                        <a href="<c:url value='/track/filterByGenre'/>"
                           class="vc-songs-sort-option ${empty selectedGenre ? 'active' : ''}">
                            <spring:message code="songs.allGenres"/>
                        </a>

                        <c:forEach items="${genres}" var="genre">

                            <c:url var="genreUrl" value="/track/filterByGenre">
                                <c:param name="genre" value="${genre}"/>
                            </c:url>

                            <a href="${genreUrl}"
                               class="vc-songs-sort-option ${selectedGenre eq genre ? 'active' : ''}">
                                <c:out value="${genre}"/>
                            </a>

                        </c:forEach>

                    </div>
                </div>

            </div>

        </div>

    </div>

    <%-- TRACK LIST --%>
    <c:choose>

        <c:when test="${not empty tracks}">

            <div class="vc-songs-list">

                <c:forEach items="${tracks}" var="t">

                    <article class="vc-song-row">

                        <div class="vc-song-main">

                            <div class="vc-song-icon">
                                <span>&#9835;</span>
                            </div>

                            <div class="vc-song-title-wrapper">

                                <h3 class="vc-song-title">
                                    ${t.title}
                                </h3>

                                <p class="vc-song-artist">
                                    ${t.artist}
                                </p>

                            </div>

                        </div>

                        <div class="vc-song-album">

                            <span class="vc-song-label">
                                <spring:message code="songs.album"/>
                            </span>

                           <a href="<c:url value='/album/details/${t.discogsId}'>
                                       <c:param name='returnUrl' value='/track/tracks'/>
                                   </c:url>">
                               ${t.album}
                           </a>

                        </div>

                        <div class="vc-song-genre">

                            <span>
                                <c:choose>
                                    <c:when test="${not empty t.release.genre}">
                                        ${t.release.genre}
                                    </c:when>
                                    <c:otherwise>
                                        —
                                    </c:otherwise>
                                </c:choose>
                            </span>

                        </div>

                        <div class="vc-song-duration">
                            ${not empty t.duration ? t.duration : '—'}
                        </div>

                        <button type="button"
                                class="vc-song-add js-open-tracklist-modal"
                                data-track-id="${t.id}"
                                data-track-title="<c:out value='${t.title}'/>"
                                data-track-artist="<c:out value='${t.artist}'/>">
                            + <spring:message code="songs.add"/>
                        </button>

                    </article>

                </c:forEach>

            </div>

        </c:when>

        <c:otherwise>

            <div class="vc-songs-empty">

                <span class="vc-empty-icon">&#9835;</span>

                <h2>
                    <spring:message code="songs.empty.title"/>
                </h2>

                <p>
                    <spring:message code="songs.empty.description"/>
                </p>

                <a href="<c:url value='/album/albums'/>">
                    <spring:message code="songs.empty.button"/>
                </a>

            </div>

        </c:otherwise>

    </c:choose>

</main>

<jsp:include page="/WEB-INF/views/layouts/create-add-modal.jsp">

    <jsp:param name="eyebrowCode" value="songs.eyebrow"/>
    <jsp:param name="titleCode" value="tracklist.modal.title"/>
    <jsp:param name="createHeadingCode" value="tracklist.create.heading"/>
    <jsp:param name="placeholderCode" value="tracklist.create.placeholder"/>
    <jsp:param name="confirmCode" value="tracklist.create.button"/>

</jsp:include>

<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
