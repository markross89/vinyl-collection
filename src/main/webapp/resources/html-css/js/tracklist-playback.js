
(function () {
    'use strict';

    const settings = document.querySelector('.vc-playback-settings');
    const playbackOptions = document.querySelectorAll('.vc-playback-option');
    const transitionSettings = document.getElementById('vc-transition-settings');
    const transitionDuration = document.getElementById('vc-transition-duration');
    const transitionValue = document.getElementById('vc-transition-value');

    const playbackTime = document.querySelector('.vc-playback-time');
    const playbackTimeValue = playbackTime
        ? playbackTime.querySelector('strong')
        : null;
    const estimatedLabel = playbackTime
        ? playbackTime.querySelector('.vc-playback-time-estimated')
        : null;

    if (!settings || !playbackOptions.length || !transitionSettings ||
        !transitionDuration || !transitionValue ||
        !playbackTime || !playbackTimeValue || !estimatedLabel) {
        return;
    }

    const saveUrl = settings.dataset.playbackUrl;
    const csrfToken = settings.dataset.csrfToken;
    const csrfHeader = settings.dataset.csrfHeader;

    const rows = document.querySelectorAll('.vc-song-row');

    const hoursLabel = playbackTime.dataset.hoursLabel;
    const minutesLabel = playbackTime.dataset.minutesLabel;
    const secondsLabel = playbackTime.dataset.secondsLabel;
    const estimatedText = estimatedLabel.dataset.estimatedLabel;

    let currentPlaybackType =
        document.querySelector('.vc-playback-option.active')?.dataset.playbackType
        || 'NORMAL';

    let saveTimeout = null;

    function parseDuration(duration) {
        if (!duration || !duration.trim()) {
            return null;
        }

        const parts = duration.trim().split(':');

        if (parts.length < 2 || parts.length > 3) {
            return null;
        }

        if (!parts.every(part => /^\d+$/.test(part))) {
            return null;
        }

        const values = parts.map(Number);

        if (values.some(value => !Number.isSafeInteger(value))) {
            return null;
        }

        if (values.slice(1).some(value => value > 59)) {
            return null;
        }

        if (values[0] < 0) {
            return null;
        }

        if (parts.length === 2) {
            return values[0] * 60 + values[1];
        }

        return values[0] * 3600 + values[1] * 60 + values[2];
    }

    function calculatePlaybackTime() {
        const durations = Array.from(rows).map(row =>
            parseDuration(row.dataset.duration)
        );

        const knownDurations = durations.filter(duration => duration !== null);
        const missingCount = durations.length - knownDurations.length;

        const knownTotal = knownDurations.reduce(
            (total, duration) => total + duration,
            0
        );

        let totalSeconds = knownTotal;

        if (missingCount > 0) {
            const estimatedDuration = knownDurations.length > 0
                ? Math.round(knownTotal / knownDurations.length)
                : 300;

            totalSeconds += estimatedDuration * missingCount;
        }

        if (currentPlaybackType === 'DJ_MIX' && durations.length > 1) {
            const transitionSeconds = Number(transitionDuration.value) || 0;

            totalSeconds = Math.max(
                0,
                totalSeconds - (durations.length - 1) * transitionSeconds
            );
        }

        return {
            totalSeconds,
            estimated: missingCount > 0
        };
    }

    function formatPlaybackTime(totalSeconds) {
        const hours = Math.floor(totalSeconds / 3600);
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;

        if (hours > 0) {
            return `${hours} ${hoursLabel} ${minutes} ${minutesLabel}`;
        }

        return `${minutes} ${minutesLabel} ${seconds} ${secondsLabel}`;
    }

    function updatePlaybackTime() {
        const result = calculatePlaybackTime();

        playbackTimeValue.textContent =
            (result.estimated ? '~' : '') +
            formatPlaybackTime(result.totalSeconds);

        estimatedLabel.textContent = estimatedText;
        estimatedLabel.hidden = !result.estimated;
    }

    function updatePlaybackSettings() {
        transitionSettings.style.display =
            currentPlaybackType === 'DJ_MIX' ? '' : 'none';

        playbackOptions.forEach(option => {
            const isActive =
                option.dataset.playbackType === currentPlaybackType;

            option.classList.toggle('active', isActive);
            option.setAttribute('aria-current', isActive ? 'true' : 'false');
        });

        updatePlaybackTime();
    }

    function updateTransitionValue() {
        transitionValue.textContent =
            transitionDuration.value + ' ' + secondsLabel;

        updatePlaybackTime();
    }

    async function saveSettings() {
        if (!saveUrl || !csrfToken || !csrfHeader) {
            console.error('Playback settings URL or CSRF data is missing.');
            return;
        }

        const params = new URLSearchParams();

        params.append('playbackType', currentPlaybackType);
        params.append('transitionDuration', transitionDuration.value);

        try {
            const response = await fetch(saveUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest',
                    [csrfHeader]: csrfToken
                },
                body: params.toString(),
                credentials: 'same-origin'
            });

            if (!response.ok) {
                console.error(
                    'Could not save playback settings:',
                    response.status
                );
            }
        } catch (error) {
            console.error('Error saving playback settings:', error);
        }
    }

    function scheduleSave() {
        clearTimeout(saveTimeout);
        saveTimeout = setTimeout(saveSettings, 400);
    }

    playbackOptions.forEach(option => {
        option.addEventListener('click', function (event) {
            event.preventDefault();

            const selectedType = this.dataset.playbackType;

            if (!selectedType || selectedType === currentPlaybackType) {
                return;
            }

            currentPlaybackType = selectedType;

            updatePlaybackSettings();
            saveSettings();
        });
    });

    transitionDuration.addEventListener('input', function () {
        updateTransitionValue();
    });

    transitionDuration.addEventListener('change', function () {
        updateTransitionValue();
        scheduleSave();
    });

    updatePlaybackSettings();
    updateTransitionValue();

})();
