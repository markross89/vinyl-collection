
function showNotification(message, type) {
    const container = document.getElementById("vc-notification-container");

    if (!container) {
        console.error("Brak elementu vc-notification-container!");
        return;
    }

    const iconNames = {
        success: "fa-check-circle",
        info: "fa-info-circle",
        error: "fa-exclamation-circle"
    };

    const alert = document.createElement("div");
    alert.className = "vc-alert vc-alert-" + type;
    alert.setAttribute("role", "status");

    const icon = document.createElement("i");
    icon.className = "fas " + (iconNames[type] || iconNames.error);

    const text = document.createElement("span");
    text.textContent = message;

    alert.appendChild(icon);
    alert.appendChild(text);
    container.prepend(alert);

    requestAnimationFrame(() => {
        alert.classList.add("vc-alert-visible");
    });

    setTimeout(() => {
        alert.classList.remove("vc-alert-visible");
        alert.classList.add("vc-alert-hiding");

        setTimeout(() => {
            alert.remove();
        }, 400);
    }, 4000);
}
(function () {
    const pendingNotification = sessionStorage.getItem(
        'vc-pending-notification'
    );

    if (!pendingNotification) {
        return;
    }

    sessionStorage.removeItem('vc-pending-notification');

    try {
        const notification = JSON.parse(pendingNotification);

        showNotification(
            notification.message,
            notification.type
        );
    } catch (error) {
        console.error('Invalid pending notification:', error);
    }

})();
(function () {
    const flashNotification = document.getElementById(
        'vc-flash-notification'
    );

    if (!flashNotification) {
        return;
    }

    const message = flashNotification.dataset.message;
    const type = flashNotification.dataset.type || 'success';

    if (message) {
        showNotification(message, type);
    }
})();