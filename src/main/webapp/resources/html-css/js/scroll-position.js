
(function () {

    // Save scroll position when opening album details
    document.addEventListener("click", function (event) {

        const link = event.target.closest(
            'a[href*="/album/details/"], a[href*="/home/details/"]'
        );

        if (!link) {
            return;
        }

        const url = new URL(link.href, window.location.href);
        const pathname = url.pathname;

        let returnTo = url.searchParams.get("returnTo");

        // Albums from the main Discogs page
        if (pathname.includes("/home/details/")) {
            returnTo = "home";
            url.searchParams.set("returnTo", "home");
            link.href = url.toString();
        }

        if (!["tracks", "albums", "home"].includes(returnTo)) {
            return;
        }

        sessionStorage.setItem(
            "vc-scroll-" + returnTo,
            window.scrollY.toString()
        );
    });

    // Identify the page to which the user has returned
    const path = window.location.pathname;

    let storageKey = null;

    if (path.endsWith("/track/tracks")) {
        storageKey = "vc-scroll-tracks";
    } else if (path.endsWith("/album/albums")) {
        storageKey = "vc-scroll-albums";
    } else if (path.endsWith("/home/start")) {
        storageKey = "vc-scroll-home";
    }

    if (!storageKey) {
        return;
    }

    const savedPosition = sessionStorage.getItem(storageKey);

    if (savedPosition !== null) {

        sessionStorage.removeItem(storageKey);

        window.addEventListener("load", function () {

            requestAnimationFrame(function () {
                requestAnimationFrame(function () {

                    window.scrollTo({
                        top: Number(savedPosition),
                        behavior: "instant"
                    });

                });
            });

        }, { once: true });
    }

})();
