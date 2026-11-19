
document.addEventListener("submit", async function (event) {

    const form = event.target.closest(".vc-add-form");

    if (!form) {
        return;
    }

    event.preventDefault();

    const button = form.querySelector("button[type='submit']");

    if (!button || button.disabled) {
        return;
    }

    button.disabled = true;

    try {
        const response = await fetch(form.action, {
            method: "POST",
            body: new FormData(form),
            headers: {
                "Accept": "application/json",
                "X-Requested-With": "XMLHttpRequest"
            },
            credentials: "same-origin"
        });

        const responseText = await response.text();

        let data;

        try {
            data = JSON.parse(responseText);
        } catch (error) {
            throw new Error("Nieprawidłowa odpowiedź serwera.");
        }

        if (!response.ok) {
            showNotification(
                data.message || "Nie udało się dodać albumu.",
                data.type || "error"
            );
            return;
        }

        showNotification(data.message, data.type);

    } catch (error) {
        console.error("Album AJAX error:", error);

        showNotification(
            "Nie udało się odebrać prawidłowej odpowiedzi z serwera.",
            "error"
        );

    } finally {
        button.disabled = false;
    }
});
