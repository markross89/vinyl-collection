
(function () {
    'use strict';

    const modal = document.getElementById('vc-delete-modal');

    if (!modal) {
        return;
    }

    const itemName = document.getElementById('vc-delete-item-name');
    const confirmButton = document.getElementById('vc-delete-confirm');
    const cancelButton = document.getElementById('vc-delete-cancel');
    const modalTitle = document.getElementById('vc-delete-title');
    const modalDescription = document.getElementById('vc-delete-description');

    const defaultTitle = modalTitle.textContent.trim();
    const defaultDescription = modalDescription.textContent.trim();

    if (!itemName || !confirmButton || !cancelButton) {
        console.error('Delete modal: required elements are missing.');
        return;
    }

    const csrfToken = modal.dataset.csrfToken;
    const csrfHeader = modal.dataset.csrfHeader;

    let deleteUrl = null;
    let deleteRedirect = null;

    // ---------------------------------------------
    // I18N
    // ---------------------------------------------

    function getMessage(key) {
        const containers = [
            document.getElementById('vc-messages'),
            document.getElementById('vc-box-messages'),
            document.getElementById('vc-tracklist-messages'),
            document.getElementById('vc-album-messages')
        ];

        for (const container of containers) {
            if (!container) {
                continue;
            }

            const element = Array.from(
                container.querySelectorAll('[data-message-key]')
            ).find(item => item.dataset.messageKey === key);

            if (element) {
                return element.textContent.trim();
            }
        }

        console.error('Delete modal: missing i18n key:', key);
        return '';
    }

    function getModuleMessages(url) {

        // Usuwanie pojedynczego utworu z Playlisty
        if (/\/tracklist\/api\/\d+\/tracks\/\d+\/?$/.test(url)) {
            return {
                success: getMessage('tracklist.removeTrack.success'),
                error: getMessage('tracklist.removeTrack.error')
            };
        }

        // Czyszczenie Playlisty
        if (/\/tracklist\/api\/\d+\/tracks\/?$/.test(url)) {
            return {
                success: getMessage('tracklist.clear.success'),
                error: getMessage('tracklist.clear.error')
            };
        }

        // Usuwanie Playlisty
        if (/\/tracklist\/api\/\d+\/?$/.test(url)) {
            return {
                success: getMessage('tracklist.delete.success'),
                error: getMessage('tracklist.delete.error')
            };
        }

        // Usuwanie albumu z kolekcji
        if (/\/album\/api\/\d+\/?$/.test(url)) {
            return {
                success: getMessage('album.remove.success'),
                error: getMessage('album.remove.error')
            };
        }

        // Usuwanie albumu z Boxa
        if (/\/box\/api\/\d+\/albums\/\d+\/?$/.test(url)) {
            return {
                success: getMessage('box.album.remove.success'),
                notFound: getMessage('box.album.remove.notFound'),
                error: getMessage('common.unexpectedError')
            };
        }

        // Usuwanie Boxa
        return {
            success: getMessage('box.delete.success'),
            error: getMessage('box.delete.error')
        };
    }

    // ---------------------------------------------
    // MODAL
    // ---------------------------------------------

    function openModal(url, name, redirect) {
        if (!url) {
            return;
        }

        deleteUrl = url;
        deleteRedirect = redirect || null;

        itemName.textContent = name || '';

        // Przywróć domyślne teksty przed każdym otwarciem.
        modalTitle.textContent = defaultTitle;
        modalDescription.textContent = defaultDescription;

        // Osobny opis dla czyszczenia playlisty.
        if (/\/tracklist\/api\/\d+\/tracks\/?$/.test(url)) {
            modalTitle.textContent = getMessage('tracklist.clear.modal.title');
            modalDescription.textContent = getMessage('tracklist.clear.modal.description');
        }

        modal.classList.add('is-visible');
        modal.setAttribute('aria-hidden', 'false');
    }

    function closeModal() {
        modal.classList.remove('is-visible');
        modal.setAttribute('aria-hidden', 'true');

        deleteUrl = null;
        deleteRedirect = null;

        itemName.textContent = '';
        confirmButton.disabled = false;
    }

    // ---------------------------------------------
    // NOTIFICATIONS
    // ---------------------------------------------

    function notify(message, type) {
        if (message && typeof showNotification === 'function') {
            showNotification(message, type);
        } else if (message) {
            console.error(message);
        }
    }

    function savePendingNotification(message, type) {
        if (!message) {
            return false;
        }

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

    function reloadAfterDelete(message, redirect) {
        if (savePendingNotification(message, 'success')) {
            if (redirect) {
                window.location.assign(redirect);
            } else {
                window.location.reload();
            }

            return;
        }

        notify(message, 'success');

        if (redirect) {
            window.location.assign(redirect);
        } else {
            window.location.reload();
        }
    }

    // ---------------------------------------------
    // DELETE REQUEST
    // ---------------------------------------------

    async function deleteItem() {
        if (!deleteUrl || confirmButton.disabled) {
            return;
        }

        confirmButton.disabled = true;

        const messages = getModuleMessages(deleteUrl);

        const headers = {
            'X-Requested-With': 'XMLHttpRequest'
        };

        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }

        try {
            const response = await fetch(deleteUrl, {
                method: 'DELETE',
                headers: headers,
                credentials: 'same-origin'
            });

            // -------------------------------------
            // ERROR RESPONSE
            // -------------------------------------

            if (!response.ok) {
                let message = messages.error;

                try {
                    const data = await response.json();

                    if (data.message) {
                        message = data.message;
                    }
                } catch (error) {
                    // Response does not contain JSON.
                }

                notify(
                    message || getMessage('common.unexpectedError'),
                    'error'
                );

                return;
            }

            // -------------------------------------
            // JSON RESPONSE
            // Used when removing an album from Box
            // -------------------------------------

            const contentType = response.headers.get('content-type') || '';

            if (contentType.includes('application/json')) {
                const data = await response.json();

                if (data.type === 'success') {
                    const redirect = deleteRedirect;

                    closeModal();
                    reloadAfterDelete(data.message || messages.success, redirect);
                    return;
                }

                if (data.type === 'info') {
                    closeModal();
                    notify(data.message || messages.notFound, 'info');
                    return;
                }

                if (data.type === 'error') {
                    notify(
                        getMessage('common.unexpectedError'),
                        'error'
                    );

                    return;
                }
            }

            // -------------------------------------
            // SUCCESS RESPONSE (HTTP 204)
            // -------------------------------------

            const redirect = deleteRedirect;

            closeModal();
            reloadAfterDelete(messages.success, redirect);

        } catch (error) {
            console.error('Delete request failed:', error);

            notify(
                getMessage('common.unexpectedError'),
                'error'
            );

        } finally {
            confirmButton.disabled = false;
        }
    }

    // ---------------------------------------------
    // EVENT LISTENERS
    // ---------------------------------------------

    document.addEventListener('click', function (event) {
        const button = event.target instanceof Element
            ? event.target.closest('.js-open-delete-modal')
            : null;

        if (!button) {
            return;
        }

        event.preventDefault();

        openModal(
            button.dataset.deleteUrl,
            button.dataset.deleteName,
            button.dataset.deleteRedirect
        );
    });

    confirmButton.addEventListener('click', deleteItem);
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
