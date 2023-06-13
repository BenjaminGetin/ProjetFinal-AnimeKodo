$(document).ready(function () {
    var pageSize = 10;  // Modifié à 10
    var currentPage = 1;
    var totalPages = Math.ceil($(".anime-card").length / pageSize);

    function loadPage(pageNumber) {
        var start = (pageNumber - 1) * pageSize;
        var end = start + pageSize;

        $(".anime-card").slice(start, end).show();
        $("#load-more").prop("disabled", pageNumber >= totalPages);
    }

    $("#load-more").click(function () {
        if (currentPage < totalPages) {
            currentPage++;
            loadPage(currentPage);
        }
    });

    // Initialize the first page
    loadPage(currentPage);
});