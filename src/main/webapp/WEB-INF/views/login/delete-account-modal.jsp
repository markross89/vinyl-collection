
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- DELETE ACCOUNT MODAL -->
<div id="vc-delete-account-modal"
     class="vc-confirm-overlay"
     aria-hidden="true">

    <div class="vc-confirm-modal"
         role="dialog"
         aria-modal="true"
         aria-labelledby="vc-delete-account-title">

        <div class="vc-confirm-icon">
            <i class="fas fa-trash-alt"></i>
        </div>

        <h3 id="vc-delete-account-title">
            <spring:message code="profile.delete.modal.title"/>
        </h3>

        <p>
            <spring:message code="profile.delete.modal.description"/>
        </p>

        <div class="vc-confirm-actions">

            <button type="button"
                    class="vc-confirm-cancel"
                    id="cancelDeleteAccount">
                <spring:message code="common.cancel"/>
            </button>

            <button type="button"
                    class="vc-confirm-delete"
                    id="confirmDeleteAccount">
                <i class="fas fa-trash-alt"></i>
                <spring:message code="common.delete.modal.confirm"/>
            </button>

        </div>
    </div>
</div>
