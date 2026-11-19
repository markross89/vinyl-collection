<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- TRACK I18N -->

<div id="vc-messages" hidden>

    <!-- TRACKLIST MODAL -->

    <span data-message-key="loading">
        <spring:message code="tracklist.modal.loading"/>
    </span>

    <span data-message-key="loadError">
        <spring:message code="tracklist.modal.load.error"/>
    </span>

    <span data-message-key="empty">
        <spring:message code="tracklist.modal.empty"/>
    </span>

    <span data-message-key="add">
        <spring:message code="tracklist.modal.add"/>
    </span>

    <span data-message-key="added">
        <spring:message code="tracklist.modal.added"/>
    </span>

    <span data-message-key="enterName">
        <spring:message code="tracklist.create.name.empty"/>
    </span>

    <span data-message-key="addSuccess">
        <spring:message code="tracklist.modal.add.success"/>
    </span>

    <span data-message-key="addError">
        <spring:message code="tracklist.modal.add.error"/>
    </span>

    <span data-message-key="createSuccess">
        <spring:message code="tracklist.createWithTrack.success"/>
    </span>
    <span data-message-key="remove">
        <spring:message code="tracklist.modal.remove"/>
    </span>

    <span data-message-key="removeSuccess">
        <spring:message code="tracklist.modal.remove.success"/>
    </span>

    <span data-message-key="removeError">
        <spring:message code="tracklist.modal.remove.error"/>
    </span>

    <span data-message-key="createError">
        <spring:message code="tracklist.createWithTrack.error"/>
    </span>

    <!-- COMMON -->

    <span data-message-key="unexpectedError">
        <spring:message code="common.unexpectedError"/>
    </span>

</div>