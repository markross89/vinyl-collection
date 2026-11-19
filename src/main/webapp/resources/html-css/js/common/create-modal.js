
(function () {
    'use strict';

    const modal = document.getElementById('vc-create-modal');

    if (!modal) {
        return;
    }

    const form = document.getElementById('vc-create-modal-form');
    const input = document.getElementById('vc-create-modal-name');
    const cancelButton = document.getElementById('vc-create-modal-cancel');
    const submitButton = document.getElementById('vc-create-modal-submit');

    const title = document.getElementById('vc-create-modal-title');
    const description = document.getElementById('vc-create-modal-description');
    const confirmText = document.getElementById('vc-create-modal-confirm');

    if (!form || !input || !cancelButton || !submitButton ||
        !title || !description || !confirmText) {
        console.error('Create modal: required elements not found.');
        return;
    }

    const csrfToken = modal.dataset.csrfToken;
    const csrfHeader = modal.dataset.csrfHeader;

    const defaultConfig = {
        createUrl: modal.dataset.createUrl,
        errorMessage: modal.dataset.errorMessage,
        title: title.textContent.trim(),
        description: description.textContent.trim(),
        placeholder: input.placeholder,
        confirm: confirmText.textContent.trim()
    };

    let activeConfig = { ...defaultConfig };

    function openModal(button) {
        activeConfig = {
            createUrl: button.dataset.createUrl || defaultConfig.createUrl,
            errorMessage: button.dataset.createError || defaultConfig.errorMessage,
            title: button.dataset.createTitle || defaultConfig.title,
            description: button.dataset.createDescription || defaultConfig.description,
            placeholder: button.dataset.createPlaceholder || defaultConfig.placeholder,
            confirm: button.dataset.createConfirm || defaultConfig.confirm
        };

        title.textContent = activeConfig.title;
        description.textContent = activeConfig.description;
        input.placeholder = activeConfig.placeholder;
        confirmText.textContent = activeConfig.confirm;

        input.value = '';

        modal.classList.add('is-visible');
        modal.setAttribute('aria-hidden', 'false');

        input.focus();
    }

    function closeModal() {
        modal.classList.remove('is-visible');
        modal.setAttribute('aria-hidden', 'true');

        form.reset();

        activeConfig = { ...defaultConfig };

        title.textContent = defaultConfig.title;
        description.textContent = defaultConfig.description;
        input.placeholder = defaultConfig.placeholder;
        confirmText.textContent = defaultConfig.confirm;

        setSubmitting(false);
    }

    function showMessage(message, type) {
        if (!message) {
            return;
        }

        if (typeof showNotification === 'function') {
            showNotification(message, type);
        } else {
            console.error(message);
        }
    }

    function savePendingNotification(message, type) {
        try {
            sessionStorage.setItem(
                'vc-pending-notification',
                JSON.stringify({
                    message: message,
                    type: type
                })
            );

            return true;
        } catch (error) {
            console.error('Could not save pending notification:', error);
            return false;
        }
    }

    function setSubmitting(isSubmitting) {
        submitButton.disabled = isSubmitting;
        input.disabled = isSubmitting;
        cancelButton.disabled = isSubmitting;
    }

    async function createItem(event) {
        event.preventDefault();

        const name = input.value.trim();

        if (!name) {
            input.focus();
            return;
        }

        if (!activeConfig.createUrl) {
            console.error('Create modal: create URL is missing.');
            return;
        }

        const headers = {
            'X-Requested-With': 'XMLHttpRequest'
        };

        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }

        setSubmitting(true);

        try {
            const response = await fetch(activeConfig.createUrl, {
                method: 'POST',
                headers: headers,
                body: new URLSearchParams({
                    name: name
                }),
                credentials: 'same-origin'
            });

            let data = {};

            try {
                data = await response.json();
            } catch (error) {
                console.error('Create modal: invalid server response.', error);
            }

            if (!response.ok) {
                showMessage(
                    data.message || activeConfig.errorMessage,
                    'error'
                );
                return;
            }

            const message = data.message;

            closeModal();

            if (message && savePendingNotification(message, 'success')) {
                window.location.reload();
                return;
            }

            showMessage(message, 'success');

        } catch (error) {
            console.error('Create modal: request failed.', error);

            showMessage(activeConfig.errorMessage, 'error');

        } finally {
            setSubmitting(false);
        }
    }

    // Event delegation – handles all create buttons.
    document.addEventListener('click', function (event) {
        const button = event.target.closest('[data-open-create-modal]');

        if (!button) {
            return;
        }

        openModal(button);
    });

    form.addEventListener('submit', createItem);

    cancelButton.addEventListener('click', closeModal);

    modal.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    document.addEventListener('keydown', function (event) {
        if (
            event.key === 'Escape' &&
            modal.classList.contains('is-visible')
        ) {
            closeModal();
        }
    });

})();
