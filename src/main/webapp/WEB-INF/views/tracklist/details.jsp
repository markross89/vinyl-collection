
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/views/tracklist/tracklist-i18n.jsp"/>
<jsp:include page="/WEB-INF/views/layouts/header.jsp"/>

<main class="vc-main vc-songs-section">

    <%-- BACK --%>
    <a href="${pageContext.request.contextPath}/tracklist/tracklists">
        <i class="fas fa-arrow-left"></i>
        <spring:message code="common.back"/>
    </a>

    <%-- SECTION HEADING --%>
    <div class="vc-section-heading">

        <div>
            <span class="vc-eyebrow">
                <spring:message code="tracklists.eyebrow"/>
            </span>

            <h2>
                <c:out value="${tracklist.name}"/>
            </h2>

            <p class="vc-section-description">
                <spring:message code="tracklist.details.description"/>
            </p>
        </div>

        <%-- DELETE TRACKLIST --%>
        <div class="vc-box-heading-actions">

            <button type="button"
                    class="vc-remove-button js-open-delete-modal"
                    data-delete-url="${pageContext.request.contextPath}/tracklist/api/${tracklist.id}"
                    data-delete-name="${fn:escapeXml(tracklist.name)}">

                <i class="fas fa-trash-alt"></i>
                <spring:message code="tracklists.delete"/>

            </button>

        </div>

    </div>

    <%-- SEARCH AND TRACK COUNT --%>
    <div class="vc-songs-overview">

        <div class="vc-songs-sidebar">

            <form class="vc-search"
                  action="<c:url value='/tracklist/${tracklist.id}/tracks/search'/>"
                  method="get">

                <i class="fas fa-search"></i>

                <input type="text"
                       name="query"
                       value="${fn:escapeXml(param.query)}"
                       placeholder="<spring:message code='tracklist.details.search.placeholder'/>"
                       aria-label="<spring:message code='tracklist.details.search.placeholder'/>"
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
                        <spring:message code="tracklist.details.trackCount.empty"/>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>

    </div>


<%-- PLAYBACK AND SORTING --%>
<div class="vc-tracklist-tools">

    <%-- FIRST ROW: PLAYBACK TYPE + SLIDER --%>
    <div class="vc-tracklist-top-row">

        <%-- PLAYBACK TYPE --%>
        <div class="vc-playback-settings"
             data-playback-url="${pageContext.request.contextPath}/tracklist/api/${tracklist.id}/playback-settings"
             data-csrf-token="<c:out value='${_csrf.token}'/>"
             data-csrf-header="<c:out value='${_csrf.headerName}'/>">

            <label class="vc-playback-type-label">
                <spring:message code="tracklist.playback.type"/>
            </label>

            <div class="vc-songs-sort vc-playback-mode">

                <a href="#"
                   class="vc-songs-sort-option vc-playback-option ${tracklist.playbackType eq 'NORMAL' ? 'active' : ''}"
                   data-playback-type="NORMAL">
                    <spring:message code="tracklist.playback.normal"/>
                </a>

                <a href="#"
                   class="vc-songs-sort-option vc-playback-option ${tracklist.playbackType eq 'DJ_MIX' ? 'active' : ''}"
                   data-playback-type="DJ_MIX">
                    <spring:message code="tracklist.playback.djmix"/>
                </a>

            </div>

        </div>

        <%-- DJ MIX TRANSITION SLIDER --%>
        <div id="vc-transition-settings"
             class="vc-transition-settings"
             style="${tracklist.playbackType eq 'DJ_MIX' ? '' : 'display: none;'}">

            <label for="vc-transition-duration">
                <spring:message code="tracklist.playback.transition"/>
            </label>

            <div class="vc-transition-control">

                <input type="range"
                       id="vc-transition-duration"
                       name="transitionDuration"
                       min="0"
                       max="180"
                       step="5"
                       value="${tracklist.transitionDuration}"/>

                <span id="vc-transition-value">
                    ${tracklist.transitionDuration}
                    <spring:message code="tracklist.playback.seconds"/>
                </span>

            </div>

        </div>

    </div>

    <%-- SECOND ROW: PLAYBACK TIME + SORTING --%>
    <div class="vc-tracklist-bottom-row">

        <%-- LEFT: PLAYBACK TIME --%>
        <div class="vc-playback-time-wrapper">

            <c:set var="totalSeconds" value="${playbackTime.totalSeconds}"/>
            <c:set var="hours" value="${(totalSeconds - (totalSeconds mod 3600)) / 3600}"/>
            <c:set var="minutes" value="${((totalSeconds mod 3600) - (totalSeconds mod 60)) / 60}"/>
            <c:set var="seconds" value="${totalSeconds mod 60}"/>

            <div class="vc-playback-time"
                 data-hours-label="<spring:message code='tracklist.playback.hours'/>"
                 data-minutes-label="<spring:message code='tracklist.playback.minutes'/>"
                 data-seconds-label="<spring:message code='tracklist.playback.seconds'/>">

                <div class="vc-playback-time-main">

                    <span class="vc-playback-time-label">
                        <spring:message code="tracklist.playback.duration"/>
                    </span>

                    <strong>
                        <c:if test="${playbackTime.estimated}">~</c:if>

                        <c:choose>
                            <c:when test="${hours gt 0}">
                                ${hours}
                                <spring:message code="tracklist.playback.hours"/>
                                ${minutes}
                                <spring:message code="tracklist.playback.minutes"/>
                            </c:when>
                            <c:otherwise>
                                ${minutes}
                                <spring:message code="tracklist.playback.minutes"/>
                                ${seconds}
                                <spring:message code="tracklist.playback.seconds"/>
                            </c:otherwise>
                        </c:choose>
                    </strong>

                </div>

                <span class="vc-playback-time-estimated"
                      data-estimated-label="<spring:message code='tracklist.playback.estimated'/>"
                      <c:if test="${not playbackTime.estimated}">hidden</c:if>>
                    <spring:message code="tracklist.playback.estimated"/>
                </span>

            </div>

        </div>

        <%-- RIGHT: SORTING AND CLEAR PLAYLIST --%>
        <div class="vc-songs-main-controls">

            <div class="vc-songs-controls">

                <div class="vc-songs-control-group">

                    <span class="vc-songs-control-label">
                        <spring:message code="songs.sortBy"/>
                    </span>

                    <div class="vc-songs-sort">

                        <a href="<c:url value='/tracklist/${tracklist.id}/tracks'><c:param name='sort' value='manual'/></c:url>"
                           class="vc-songs-sort-option ${activeSort eq 'manual' ? 'active' : ''}">
                            <spring:message code="tracklist.sort.manual"/>
                        </a>

                        <a href="<c:url value='/tracklist/${tracklist.id}/tracks'><c:param name='sort' value='artist'/></c:url>"
                           class="vc-songs-sort-option ${activeSort eq 'artist' ? 'active' : ''}">
                            <spring:message code="songs.sort.artist"/>
                        </a>

                        <a href="<c:url value='/tracklist/${tracklist.id}/tracks'><c:param name='sort' value='title'/></c:url>"
                           class="vc-songs-sort-option ${activeSort eq 'title' ? 'active' : ''}">
                            <spring:message code="songs.sort.title"/>
                        </a>

                        <a href="<c:url value='/tracklist/${tracklist.id}/tracks'><c:param name='sort' value='album'/></c:url>"
                           class="vc-songs-sort-option ${activeSort eq 'album' ? 'active' : ''}">
                            <spring:message code="songs.sort.album"/>
                        </a>

                        <a href="<c:url value='/tracklist/${tracklist.id}/tracks'><c:param name='sort' value='duration'/></c:url>"
                           class="vc-songs-sort-option ${activeSort eq 'duration' ? 'active' : ''}">
                            <spring:message code="songs.sort.duration"/>
                        </a>

                        <a href="<c:url value='/tracklist/${tracklist.id}/tracks'><c:param name='sort' value='label'/></c:url>"
                           class="vc-songs-sort-option ${activeSort eq 'label' ? 'active' : ''}">
                            <spring:message code="songs.sort.label"/>
                        </a>

                        <a href="<c:url value='/tracklist/${tracklist.id}/tracks'><c:param name='sort' value='date'/></c:url>"
                           class="vc-songs-sort-option ${activeSort eq 'date' ? 'active' : ''}">
                            <spring:message code="songs.sort.date"/>
                        </a>

                    </div>

                </div>

            </div>

            <c:if test="${not empty tracks}">

                <div class="vc-tracklist-clear-action">

                    <a href="#"
                       class="js-open-delete-modal"
                       data-delete-url="${pageContext.request.contextPath}/tracklist/api/${tracklist.id}/tracks"
                       data-delete-name="${fn:escapeXml(tracklist.name)}">

                        <i class="fas fa-trash-alt"></i>
                        <spring:message code="tracklist.clear"/>

                    </a>

                </div>

            </c:if>

        </div>

    </div>

</div>

    <%-- TRACK LIST --%>
    <c:choose>

        <c:when test="${not empty tracks}">

            <div class="vc-songs-list"
                 data-tracklist-id="${tracklist.id}"
                 data-reorder-url="${pageContext.request.contextPath}/tracklist/api/${tracklist.id}/tracks/reorder"
                 data-sort-mode="${activeSort}">

                <c:forEach items="${tracks}" var="t" varStatus="status">

                    <article class="vc-song-row"
                             data-track-id="${t.id}"
                             data-duration="<c:out value='${t.duration}'/>">

                        <div class="vc-song-position">
                            ${status.index + 1}
                        </div>

                        <span class="vc-song-drag-handle ${activeSort ne 'manual' ? 'is-disabled' : ''}"
                              aria-hidden="true">
                            <i class="fas fa-grip-vertical"></i>
                        </span>

                        <div class="vc-song-main">

                            <div class="vc-song-icon">
                                <span>&#9835;</span>
                            </div>

                            <div class="vc-song-title-wrapper">

                                <h3 class="vc-song-title">
                                    <c:out value="${t.title}"/>
                                </h3>

                                <p class="vc-song-artist">
                                    <c:out value="${t.artist}"/>
                                </p>

                            </div>

                        </div>

                        <div class="vc-song-album">

                            <span class="vc-song-label">
                                <spring:message code="songs.album"/>
                            </span>

                            <a href="<c:url value='/album/details/${t.discogsId}'>
                                        <c:param name='returnUrl' value='/tracklist/${tracklist.id}'/>
                                    </c:url>">
                                <c:out value="${t.album}"/>
                            </a>

                        </div>

                        <div class="vc-song-genre">

                            <span>
                                <c:choose>
                                    <c:when test="${not empty t.release.genre}">
                                        <c:out value="${t.release.genre}"/>
                                    </c:when>

                                    <c:otherwise>
                                        —
                                    </c:otherwise>
                                </c:choose>
                            </span>

                        </div>

                        <div class="vc-song-duration">
                            <c:out value="${not empty t.duration ? t.duration : '—'}"/>
                        </div>

                        <button type="button"
                                class="vc-remove-button js-open-delete-modal"
                                data-delete-url="${pageContext.request.contextPath}/tracklist/api/${tracklist.id}"
                                data-delete-name="${fn:escapeXml(tracklist.name)}"
                                data-delete-redirect="${pageContext.request.contextPath}/tracklist/tracklists">

                            <i class="fas fa-trash-alt"></i>
                            <spring:message code="tracklists.delete"/>

                        </button>

                    </article>

                </c:forEach>

            </div>

        </c:when>

        <c:otherwise>

            <div class="vc-empty-state">

                <i class="fas fa-music"></i>

                <h2>
                    <spring:message code="tracklist.details.empty.title"/>
                </h2>

                <p>
                    <spring:message code="tracklist.details.empty.description"/>
                </p>

                <a href="${pageContext.request.contextPath}/track/tracks"
                   class="vc-add-button">

                    <i class="fas fa-plus"></i>
                    <spring:message code="tracklist.details.empty.button"/>

                </a>

            </div>

        </c:otherwise>

    </c:choose>

</main>



<jsp:include page="/WEB-INF/views/layouts/footer.jsp"/>
