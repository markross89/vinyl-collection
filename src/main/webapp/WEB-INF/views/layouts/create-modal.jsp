
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- UNIVERSAL CREATE MODAL -->

<div id="vc-create-modal"
     class="vc-confirm-overlay"
     aria-hidden="true"
     data-create-url="${requestScope.createModalUrl}"
     data-csrf-token="${_csrf.token}"
     data-csrf-header="${_csrf.headerName}"
     data-error-message="<spring:message code='${requestScope.createModalErrorCode}'/>">

    <div class="vc-confirm-modal"
         role="dialog"
         aria-modal="true"
         aria-labelledby="vc-create-modal-title">

        <div class="vc-confirm-icon">
            <i class="fas fa-folder-plus"></i>
        </div>

        <h3 id="vc-create-modal-title">
            <spring:message code="${requestScope.createModalTitleCode}"/>
        </h3>

        <p id="vc-create-modal-description">
            <spring:message code="${requestScope.createModalDescriptionCode}"/>
        </p>

        <form id="vc-create-modal-form" method="post">

            <input type="text"
                   name="name"
                   id="vc-create-modal-name"
                   class="vc-create-box-input"
                   maxlength="100"
                   required
                   autocomplete="off"
                   placeholder="<spring:message code='${requestScope.createModalPlaceholderCode}'/>"/>

            <div class="vc-confirm-actions">

                <button type="button"
                        class="vc-confirm-cancel"
                        id="vc-create-modal-cancel">
                    <spring:message code="common.cancel"/>
                </button>

                <button type="submit"
                        class="vc-confirm-delete"
                        id="vc-create-modal-submit">
                    <i class="fas fa-plus"></i>
                    <span id="vc-create-modal-confirm">
                        <spring:message code="${requestScope.createModalConfirmCode}"/>
                    </span>
                </button>

            </div>

        </form>

    </div>
</div>


