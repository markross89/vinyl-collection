
(function () {
    'use strict';

    const modal = document.getElementById('vc-delete-account-modal');
    const openButton = document.getElementById('openDeleteAccountModal');
    const cancelButton = document.getElementById('cancelDeleteAccount');
    const confirmButton = document.getElementById('confirmDeleteAccount');
    const deleteForm = document.getElementById('deleteAccountForm');

    if (!modal || !openButton || !cancelButton ||
        !confirmButton || !deleteForm) {
        return;
    }

    function openModal() {
        modal.classList.add('is-visible');
        modal.setAttribute('aria-hidden', 'false');

        cancelButton.focus();
    }

    function closeModal() {
        modal.classList.remove('is-visible');
        modal.setAttribute('aria-hidden', 'true');

        openButton.focus();
    }

    // Open modal
    openButton.addEventListener('click', openModal);

    // Cancel deletion
    cancelButton.addEventListener('click', closeModal);

    // Confirm deletion
    confirmButton.addEventListener('click', function () {
        confirmButton.disabled = true;
        deleteForm.submit();
    });

    // Close when clicking outside the modal
    modal.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    // Close with Escape key
    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape' &&
            modal.classList.contains('is-visible')) {
            closeModal();
        }
    });

})();
