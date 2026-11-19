
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- CREATE / ADD MODAL -->

<div id="vc-create-add-modal"
     class="vc-modal-overlay"
     aria-hidden="true"
     data-context-path="${pageContext.request.contextPath}"
     data-csrf-token="${_csrf.token}"
     data-csrf-header="${_csrf.headerName}">

    <div class="vc-modal"
         role="dialog"
         aria-modal="true"
         aria-labelledby="vc-create-add-title">

        <!-- MODAL HEADER -->

        <div class="vc-modal-header">

            <div>
                <span class="vc-eyebrow"
                      id="vc-create-add-eyebrow">
                    <spring:message code="${param.eyebrowCode}"/>
                </span>

                <h2 id="vc-create-add-title">
                    <spring:message code="${param.titleCode}"/>
                </h2>

                <p id="vc-create-add-description"
                   class="vc-modal-subtitle"></p>
            </div>

            <button type="button"
                    class="vc-modal-close"
                    id="vc-create-add-cancel"
                    aria-label="<spring:message code='common.close'/>">

                <i class="fas fa-times"></i>

            </button>

        </div>

        <!-- MODAL BODY -->

        <div class="vc-modal-body">

            <!-- EXISTING BOXES / TRACKLISTS -->

            <div id="vc-create-add-list"
                 class="vc-box-list"
                 aria-live="polite">
            </div>

            <!-- CREATE NEW BOX / TRACKLIST -->

            <div class="vc-create-box">

                <div class="vc-create-box-heading">
                    <i class="fas fa-plus-circle"></i>

                    <span>
                        <spring:message code="${param.createHeadingCode}"/>
                    </span>
                </div>

                <form id="vc-create-add-form">

                    <input type="text"
                           name="name"
                           id="vc-create-add-name"
                           maxlength="100"
                           placeholder="<spring:message code='${param.placeholderCode}'/>"
                           autocomplete="off"
                           required/>

                    <button type="submit"
                            class="vc-create-box-button"
                            id="vc-create-add-confirm">

                        <i class="fas fa-plus"></i>

                        <span>
                            <spring:message code="${param.confirmCode}"/>
                        </span>

                    </button>

                </form>

            </div>

        </div>

    </div>

</div>


