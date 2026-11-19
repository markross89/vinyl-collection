
(function () {
    'use strict';

    const list = document.querySelector(
        '.vc-songs-list[data-sort-mode="manual"]'
    );

    // Drag & drop działa wyłącznie w trybie My Order.
    if (!list) {
        return;
    }

    const dragHandles = list.querySelectorAll('.vc-song-drag-handle');
    const deleteModal = document.getElementById('vc-delete-modal');

    const reorderUrl = list.dataset.reorderUrl;

    if (!reorderUrl || dragHandles.length === 0) {
        return;
    }

    const csrfToken = deleteModal ? deleteModal.dataset.csrfToken : null;
    const csrfHeader = deleteModal ? deleteModal.dataset.csrfHeader : null;

    let draggedRow = null;
    let originalOrder = [];
    let dropTargetRow = null;

    // ---------------------------------------------
    // I18N
    // ---------------------------------------------

    function getMessage(key) {
        const container = document.getElementById('vc-tracklist-messages');

        if (!container) {
            return '';
        }

        const element = Array.from(
            container.querySelectorAll('[data-message-key]')
        ).find(item => item.dataset.messageKey === key);

        return element ? element.textContent.trim() : '';
    }

    // ---------------------------------------------
    // ROWS AND NUMBERING
    // ---------------------------------------------

    function getRows() {
        return Array.from(
            list.querySelectorAll('.vc-song-row[data-track-id]')
        );
    }

    function updatePositions() {
        getRows().forEach((row, index) => {
            const position = row.querySelector('.vc-song-position');

            if (position) {
                position.textContent = index + 1;
            }
        });
    }

    function getTrackIds() {
        return getRows().map(row => Number(row.dataset.trackId));
    }

    // ---------------------------------------------
    // DROP INDICATOR
    // ---------------------------------------------

    function clearDropIndicator() {
        getRows().forEach(row => {
            row.classList.remove(
                'is-drop-before',
                'is-drop-after'
            );
        });

        dropTargetRow = null;
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

    // ---------------------------------------------
    // DRAG & DROP
    // ---------------------------------------------

    dragHandles.forEach(handle => {
        handle.setAttribute('draggable', 'true');

        handle.addEventListener('dragstart', function (event) {
            draggedRow = handle.closest('.vc-song-row');

            if (!draggedRow) {
                event.preventDefault();
                return;
            }

            originalOrder = getRows();

            clearDropIndicator();

            draggedRow.classList.add('is-dragging');

            event.dataTransfer.effectAllowed = 'move';
            event.dataTransfer.setData(
                'text/plain',
                draggedRow.dataset.trackId
            );
        });

        handle.addEventListener('dragend', function () {
            if (draggedRow) {
                draggedRow.classList.remove('is-dragging');
            }

            clearDropIndicator();

            draggedRow = null;
        });
    });

    list.addEventListener('dragover', function (event) {
        if (!draggedRow) {
            return;
        }

        event.preventDefault();

        event.dataTransfer.dropEffect = 'move';

        const targetRow = event.target.closest('.vc-song-row');

        if (!targetRow || targetRow === draggedRow) {
            clearDropIndicator();
            return;
        }

        const rect = targetRow.getBoundingClientRect();
        const insertAfter = event.clientY > rect.top + rect.height / 2;

        // Jeżeli wskaźnik jest już w tym samym miejscu,
        // nie wykonuj ponownie niepotrzebnych zmian DOM.
        const indicatorClass = insertAfter
            ? 'is-drop-after'
            : 'is-drop-before';

        if (
            dropTargetRow === targetRow &&
            targetRow.classList.contains(indicatorClass)
        ) {
            return;
        }

        // Usuń wskaźnik z poprzedniego miejsca.
        clearDropIndicator();

        // Pokaż linię w aktualnym miejscu docelowym.
        targetRow.classList.add(indicatorClass);
        dropTargetRow = targetRow;

        // Przenieś przeciągany utwór.
        if (insertAfter) {
            targetRow.after(draggedRow);
        } else {
            targetRow.before(draggedRow);
        }

        updatePositions();
    });

    list.addEventListener('drop', async function (event) {
        if (!draggedRow) {
            return;
        }

        event.preventDefault();

        clearDropIndicator();

        const newOrder = getRows();

        const hasChanged = newOrder.some(
            (row, index) => row !== originalOrder[index]
        );

        if (!hasChanged) {
            return;
        }

        await saveOrder();
    });

    // ---------------------------------------------
    // SAVE ORDER
    // ---------------------------------------------

    async function saveOrder() {
        const headers = {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        };

        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }

        try {
            const response = await fetch(reorderUrl, {
                method: 'POST',
                headers: headers,
                credentials: 'same-origin',
                body: JSON.stringify(getTrackIds())
            });

            if (!response.ok) {
                throw new Error(
                    'Reorder request failed: ' + response.status
                );
            }

            notify(
                getMessage('tracklist.reorder.success'),
                'success'
            );

        } catch (error) {
            console.error('Could not save track order:', error);

            // Przywrócenie kolejności sprzed przeciągania.
            originalOrder.forEach(row => {
                list.appendChild(row);
            });

            updatePositions();

            notify(
                getMessage('tracklist.reorder.error'),
                'error'
            );
        }
    }

    updatePositions();

})();
