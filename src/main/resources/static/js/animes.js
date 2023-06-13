$(document).ready(function () {
    var currentPage = 1;
    var pageSize = 12;

    function loadPage(pageNumber, elements) {
        var start = (pageNumber - 1) * pageSize;
        var end = start + pageSize;

        elements.slice(start, end).show();

        $("#load-more").prop("disabled", pageNumber >= Math.ceil(elements.length / pageSize));
    }

    function filterAnimes() {
        var title = $("#title-input").val();
        var subtype = $("#subtype-input").val();
        var sortBy = $("#sort-by-input").val();
        var status = $("#status-input").val();

        // Si tous les champs de recherche sont vides, charger tous les animes
        if (title === "" && subtype === "" && sortBy === "" && status === "") {
            loadAllAnimes();
            return;
        }

        var parameters = {
            title: title,
            subtype: subtype,
            sortBy: sortBy,
            status: status
        };

        var queryString = $.param(parameters);
        var container = $("#anime-container");
        container.empty();

        $.get("/api/animes?" + queryString, function(data) {
            data.forEach(function(anime) {
                var animeLink = $("<a>", { href: "/animes/" + anime.id }); // Ajouter le lien avec l'ID de l'anime
                var animeCard = $("<div>", { class: "anime-card" }).append(animeLink);
                var animeImage = $("<img>", { src: anime.image, alt: "Image Anime" });
                var animeCardContent = $("<div>", { class: "anime-card-content" });
                var animeTitle = $("<h3>").text(anime.title);
                var animeSynopsis = $("<p>").text(anime.synopsis);

                animeCardContent.append(animeTitle, animeSynopsis);
                animeLink.append(animeImage, animeCardContent); // Ajouter le contenu dans le lien
                animeCard.hide();

                container.append(animeCard);
            });

            var elements = $(".anime-card");
            loadPage(currentPage, elements);
        });
    }

    function loadAllAnimes() {
        $.get("/api/animes", function(data) {
            data.forEach(function(anime) {
                var animeLink = $("<a>", { href: "/animes/" + anime.id }); // Ajouter le lien avec l'ID de l'anime
                var animeCard = $("<div>", { class: "anime-card" }).append(animeLink);
                var animeImage = $("<img>", { src: anime.image, alt: "Image Anime" });
                var animeCardContent = $("<div>", { class: "anime-card-content" });
                var animeTitle = $("<h3>").text(anime.title);
                var animeSynopsis = $("<p>").text(anime.synopsis);

                animeCardContent.append(animeTitle, animeSynopsis);
                animeLink.append(animeImage, animeCardContent); // Ajouter le contenu dans le lien
                animeCard.hide();

                $("#anime-container").append(animeCard);
            });

            var elements = $(".anime-card");
            loadPage(currentPage, elements);
        });
    }

    $("#search-form").on("submit", function(e) {
        e.preventDefault();
        currentPage = 1; // Réinitialiser la page courante à 1 lors de la soumission d'une nouvelle recherche
        filterAnimes();
    });

    $("#load-more").click(function() {
        var elements = $(".anime-card");
        currentPage++;
        loadPage(currentPage, elements);
    });

    $("#advanced-search-toggle").click(function() {
        $("#advanced-search").toggle();
    });

    // Chargement initial des animes
    loadAllAnimes();
});
