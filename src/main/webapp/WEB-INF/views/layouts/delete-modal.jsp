
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- SHARED DELETE CONFIRMATION MODAL -->

<div id="vc-delete-modal"
     class="vc-confirm-overlay"
     aria-hidden="true"
     data-csrf-token="${_csrf.token}"
     data-csrf-header="${_csrf.headerName}">

    <div class="vc-confirm-modal"
         role="dialog"
         aria-modal="true"
         aria-labelledby="vc-delete-title">

        <div class="vc-confirm-icon">
            <i class="fas fa-trash-alt"></i>
        </div>

        <h3 id="vc-delete-title">
            <spring:message code="common.delete.modal.title"/>
        </h3>

        <p id="vc-delete-description">
            <spring:message code="common.delete.modal.description"/>
        </p>
        <p id="vc-delete-item-name" class="vc-confirm-box-name"></p>

        <div class="vc-confirm-actions">

            <button type="button"
                    class="vc-confirm-cancel"
                    id="vc-delete-cancel">
                <spring:message code="common.cancel"/>
            </button>

            <button type="button"
                    class="vc-confirm-delete"
                    id="vc-delete-confirm">
                <i class="fas fa-trash-alt"></i>
                <spring:message code="common.delete.modal.confirm"/>
            </button>

        </div>

    </div>

</div>

