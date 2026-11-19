
(function () {
    'use strict';

    const modal = document.getElementById('vc-create-add-modal');

    if (!modal) {
        return;
    }

    // MODAL ELEMENTS

    const title = document.getElementById('vc-create-add-title');
    const description = document.getElementById('vc-create-add-description');
    const list = document.getElementById('vc-create-add-list');
    const form = document.getElementById('vc-create-add-form');
    const nameInput = document.getElementById('vc-create-add-name');
    const confirmButton = document.getElementById('vc-create-add-confirm');
    const cancelButton = document.getElementById('vc-create-add-cancel');

    // CONFIGURATION

    const contextPath = modal.dataset.contextPath || '';
    const csrfToken = modal.dataset.csrfToken || '';
    const csrfHeader = modal.dataset.csrfHeader || '';

    const messageContainer = document.getElementById('vc-messages');

    let activeItemId = null;
    let activeMode = null;
    let isSubmitting = false;

    // TRANSLATIONS

    function getMessage(key) {
        if (!messageContainer) {
            return '';
        }

        const element = messageContainer.querySelector(
            '[data-message-key="' + key + '"]'
        );

        return element ? element.textContent.trim() : '';
    }

    // NOTIFICATIONS

    function notify(message, type) {
        if (typeof showNotification === 'function') {
            showNotification(message, type);
            return;
        }

        console.error('showNotification() is not available.');
    }

    // HTTP HELPERS

    function getHeaders() {
        const headers = {
            'Accept': 'application/json'
        };

        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }

        return headers;
    }

    async function readResponse(response) {
        const contentType = response.headers.get('content-type') || '';

        if (contentType.includes('application/json')) {
            return await response.json();
        }

        return {};
    }

    function getConfig(mode) {
        if (mode === 'box') {
            return {
                mode: 'box',

                collectionName: 'boxes',

                loadUrl: function (itemId) {
                    return '/box/api/albums/' +
                        encodeURIComponent(itemId);
                },

                addUrl: function (collectionId, itemId) {
                    return '/box/api/' +
                        encodeURIComponent(collectionId) +
                        '/albums/' +
                        encodeURIComponent(itemId);
                },

                removeUrl: function (collectionId, itemId) {
                    return '/box/api/' +
                        encodeURIComponent(collectionId) +
                        '/albums/' +
                        encodeURIComponent(itemId);
                },

                createUrl: '/box/api/create',

                itemIdField: 'albumId',
                containsField: 'containsAlbum',

                loadingMessage: 'loading',
                loadErrorMessage: 'loadError',
                emptyMessage: 'empty',
                addMessage: 'add',
                alreadyAddedText: 'remove',

                addSuccessMessage: 'addSuccess',
                addErrorMessage: 'updateError',

                removeSuccessMessage: 'removeSuccess',
                removeErrorMessage: 'removeError',

                createSuccessMessage: 'createSuccess',
                createErrorMessage: 'createError'
            };
        }

        if (mode === 'tracklist') {
            return {
                mode: 'tracklist',

                collectionName: 'tracklists',

                loadUrl: function (itemId) {
                    return '/track/api/tracks/' +
                        encodeURIComponent(itemId) +
                        '/tracklists';
                },

                addUrl: function (collectionId, itemId) {
                    return '/track/api/tracklists/' +
                        encodeURIComponent(collectionId) +
                        '/tracks/' +
                        encodeURIComponent(itemId);
                },
                removeUrl: function (collectionId, itemId) {
                    return '/tracklist/api/' +
                        encodeURIComponent(collectionId) +
                        '/tracks/' +
                        encodeURIComponent(itemId);
                },

                createUrl: '/tracklist/api/create-with-track',

                itemIdField: 'trackId',
                containsField: 'containsTrack',

                loadingMessage: 'loading',
                loadErrorMessage: 'loadError',
                emptyMessage: 'empty',
                addMessage: 'add',
                alreadyAddedText: 'remove',
                removeSuccessMessage: 'removeSuccess',
                removeErrorMessage: 'removeError',

                addSuccessMessage: 'addSuccess',
                addErrorMessage: 'addError',

                createSuccessMessage: 'createSuccess',
                createErrorMessage: 'createError'
            };
        }

        return null;
    }

    // MODAL STATE

    function openModal() {
        modal.classList.add('vc-modal-open');
        modal.setAttribute('aria-hidden', 'false');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        modal.classList.remove('vc-modal-open');
        modal.setAttribute('aria-hidden', 'true');
        document.body.style.overflow = '';

        activeItemId = null;
        activeMode = null;
        isSubmitting = false;

        form.reset();
        list.innerHTML = '';
        description.textContent = '';
    }

    function setSubmitting(submitting) {
        isSubmitting = submitting;
        confirmButton.disabled = submitting;
        nameInput.disabled = submitting;
    }

    // LIST RENDERING

    function showLoading(config) {
        list.innerHTML = '';

        const message = document.createElement('p');
        message.className = 'vc-box-message';
        message.textContent = getMessage(config.loadingMessage);

        list.appendChild(message);
    }

    function showEmpty(config) {
        list.innerHTML = '';

        const message = document.createElement('p');
        message.className = 'vc-box-message';
        message.textContent = getMessage(config.emptyMessage);

        list.appendChild(message);
    }

    function renderCollections(collections, config) {
        list.innerHTML = '';

        if (!Array.isArray(collections) || collections.length === 0) {
            showEmpty(config);
            return;
        }

        collections.forEach(function (collection) {
            const item = document.createElement('div');
            item.className = 'vc-box-item';

            const collectionName = document.createElement('span');
            collectionName.className = 'vc-box-name';
            collectionName.textContent = collection.name || '';

            const action = document.createElement('button');
            action.type = 'button';
            action.className = 'vc-box-action';

            const alreadyAdded = Boolean(
                collection[config.containsField]
            );

            if (alreadyAdded && config.removeUrl) {
                action.classList.add('vc-box-action-remove');

                const icon = document.createElement('i');
                icon.className = 'fas fa-minus';

                const text = document.createElement('span');
                text.textContent = getMessage(config.alreadyAddedText);

                action.appendChild(icon);
                action.appendChild(text);

                action.addEventListener('click', function () {
                    removeFromCollection(collection.id, action, config);
                });

            } else if (alreadyAdded) {
                action.classList.add('vc-box-action-add');
                action.disabled = true;
                action.textContent = getMessage(config.alreadyAddedText);

            } else {
                action.classList.add('vc-box-action-add');

                const icon = document.createElement('i');
                icon.className = 'fas fa-plus';

                const text = document.createElement('span');
                text.textContent = getMessage(config.addMessage);

                action.appendChild(icon);
                action.appendChild(text);

                action.addEventListener('click', function () {
                    addToCollection(collection.id, action, config);
                });
            }

            item.appendChild(collectionName);
            item.appendChild(action);

            list.appendChild(item);
        });
    }

    // LOAD COLLECTIONS

    async function loadCollections(itemId, config) {
        showLoading(config);

        try {
            const response = await fetch(
                contextPath + config.loadUrl(itemId),
                {
                    method: 'GET',
                    headers: getHeaders(),
                    credentials: 'same-origin'
                }
            );

            const data = await readResponse(response);

            if (!response.ok) {
                throw new Error(
                    data.message || getMessage(config.loadErrorMessage)
                );
            }

            if (
                activeItemId !== itemId ||
                activeMode !== config.mode
            ) {
                return;
            }

            renderCollections(
                Array.isArray(data) ? data : data[config.collectionName],
                config
            );
        } catch (error) {
            console.error(error);

            if (
                activeItemId !== itemId ||
                activeMode !== config.mode
            ) {
                return;
            }

            list.innerHTML = '';

            const message = document.createElement('p');
            message.className = 'vc-box-message';
            message.textContent =
                error.message || getMessage(config.loadErrorMessage);

            list.appendChild(message);
        }
    }

    // ADD ITEM TO COLLECTION

    async function addToCollection(collectionId, button, config) {
        if (!activeItemId || isSubmitting) {
            return;
        }

        button.disabled = true;

        try {
            const response = await fetch(
                contextPath + config.addUrl(collectionId, activeItemId),
                {
                    method: 'POST',
                    headers: getHeaders(),
                    credentials: 'same-origin'
                }
            );

            const data = await readResponse(response);

            if (!response.ok) {
                throw new Error(
                    data.message || getMessage(config.addErrorMessage)
                );
            }

            notify(
                data.message || getMessage(config.addSuccessMessage),
                data.type || 'success'
            );

            await loadCollections(activeItemId, config);

        } catch (error) {
            console.error(error);

            notify(
                error.message || getMessage(config.addErrorMessage),
                'error'
            );

            button.disabled = false;
        }
    }

    // REMOVE ITEM FROM COLLECTION

    async function removeFromCollection(collectionId, button, config) {
        if (!activeItemId || isSubmitting || !config.removeUrl) {
            return;
        }

        button.disabled = true;

        try {
            const response = await fetch(
                contextPath + config.removeUrl(collectionId, activeItemId),
                {
                    method: 'DELETE',
                    headers: getHeaders(),
                    credentials: 'same-origin'
                }
            );

            const data = await readResponse(response);

            if (!response.ok) {
                throw new Error(
                    data.message || getMessage(config.removeErrorMessage)
                );
            }

            notify(
                data.message || getMessage(config.removeSuccessMessage),
                data.type || 'success'
            );

            await loadCollections(activeItemId, config);

        } catch (error) {
            console.error(error);

            notify(
                error.message || getMessage(config.removeErrorMessage),
                'error'
            );

            button.disabled = false;
        }
    }

    // CREATE COLLECTION

    async function createNew(event) {
        event.preventDefault();

        if (!activeItemId || isSubmitting || !activeMode) {
            return;
        }

        const config = getConfig(activeMode);
        const name = nameInput.value.trim();

        if (!name) {
            notify(getMessage('enterName'), 'error');
            nameInput.focus();
            return;
        }

        setSubmitting(true);

        try {
            const body = new URLSearchParams();

            body.append('name', name);
            body.append(config.itemIdField, activeItemId);

            const headers = getHeaders();
            headers['Content-Type'] =
                'application/x-www-form-urlencoded;charset=UTF-8';

            const response = await fetch(
                contextPath + config.createUrl,
                {
                    method: 'POST',
                    headers: headers,
                    body: body.toString(),
                    credentials: 'same-origin'
                }
            );

            const data = await readResponse(response);

            if (!response.ok) {
                throw new Error(
                    data.message || getMessage(config.createErrorMessage)
                );
            }

            notify(
                data.message || getMessage(config.createSuccessMessage),
                data.type || 'success'
            );

            form.reset();

            await loadCollections(activeItemId, config);

        } catch (error) {
            console.error(error);

            notify(
                error.message || getMessage(config.createErrorMessage),
                'error'
            );

        } finally {
            setSubmitting(false);
        }
    }

    // OPEN MODAL BUTTONS

    document.addEventListener('click', function (event) {
        const boxButton = event.target.closest('.js-open-box-modal');
        const trackButton = event.target.closest('.js-open-tracklist-modal');

        const button = boxButton || trackButton;

        if (!button) {
            return;
        }

        const mode = boxButton ? 'box' : 'tracklist';
        const config = getConfig(mode);

        if (!config) {
            return;
        }

        const itemId = mode === 'box'
            ? button.dataset.albumId
            : button.dataset.trackId;

        if (!itemId) {
            return;
        }

        activeItemId = itemId;
        activeMode = mode;

        const itemTitle = mode === 'box'
            ? button.dataset.albumTitle
            : button.dataset.trackTitle + ' — ' + button.dataset.trackArtist;

        description.textContent = itemTitle || '';

        openModal();

        loadCollections(itemId, config);
    });

    // CLOSE MODAL

    cancelButton.addEventListener('click', closeModal);

    modal.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    document.addEventListener('keydown', function (event) {
        if (
            event.key === 'Escape' &&
            modal.classList.contains('active')
        ) {
            closeModal();
        }
    });

    // CREATE FORM

    form.addEventListener('submit', createNew);

})();
