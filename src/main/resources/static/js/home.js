$(document).ready(function () {
    $('.slider').slick({
        slidesToShow: 3,
        slidesToScroll: 3,
        dots: true,
        autoplay: true,
        autoplaySpeed: 15000,
    });
});

const slideElements = document.getElementsByClassName('slide');

for (const slideElement of slideElements) {
    const synopsisElement = slideElement.querySelector('.slide-synopsis');
    const synopsisText = synopsisElement.innerText;

    if (synopsisText.length > 100) {
        const truncatedText = synopsisText.substring(0, 100).trim();
        const remainingText = synopsisText.substring(100).trim();

        synopsisElement.innerHTML = truncatedText + '...';

        const detailsLinkElement = slideElement.querySelector('.slide-details-link');
        const detailsLinkUrl = detailsLinkElement.getAttribute('href');

        const toggleDetails = () => {
            if (synopsisElement.innerHTML === truncatedText + '...') {
                synopsisElement.innerHTML = truncatedText + ' ' + remainingText;
                detailsLinkElement.innerHTML = '<< Réduire';
            } else {
                synopsisElement.innerHTML = truncatedText + '...';
                detailsLinkElement.innerHTML = 'Détails >>';
            }
        };

        detailsLinkElement.addEventListener('click', toggleDetails);
    }
}
