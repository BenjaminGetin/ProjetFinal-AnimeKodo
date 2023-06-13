document.addEventListener('DOMContentLoaded', () => {
    const heartButton = document.querySelector('.heart-button');

    const animeId = heartButton.getAttribute('data-anime-id');
    const userId = heartButton.getAttribute('data-user-id');

    // Récupérez l'état initial de la watchlist pour cet anime
    fetch(`/api/users/${userId}/watchlist/is-in-watchlist/${animeId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.isInWatchlist) {
                heartButton.classList.add('red');
            }
        });

    heartButton.addEventListener('click', () => {
        if (!userId) {
            window.location.href = '/login'; // Redirection vers la page de connexion
            return;
        }

        if (heartButton.classList.contains('red')) {
            // L'anime est déjà dans la watchlist, donc on le supprime
            fetch(`/api/users/${userId}/watchlist/remove-anime/${animeId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
                .then(response => {
                    if (response.ok) {
                        heartButton.classList.remove('red');
                        console.log('Anime removed from watchlist');
                    } else {
                        console.error('Failed to remove anime from watchlist');
                    }
                })
                .catch(error => {
                    console.error(error);
                });
        } else {
            // L'anime n'est pas dans la watchlist, donc on l'ajoute
            fetch(`/api/users/${userId}/watchlist/add-anime/${animeId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
                .then(response => {
                    if (response.ok) {
                        heartButton.classList.add('red');
                        console.log('Anime added to watchlist');
                    } else {
                        console.error('Failed to add anime to watchlist');
                    }
                })
                .catch(error => {
                    console.error(error);
                });
        }
    });
});

const ratingForm = document.getElementById('ratingForm');
const ratingInputs = ratingForm.elements.selectedRating;

for (let i = 0; i < ratingInputs.length; i++) {
    ratingInputs[i].addEventListener('change', function () {
        ratingForm.submit();
    });
}
function showEditCommentForm(element) {
    var commentId = element.getAttribute('data-comment-id');
    var animeId = element.getAttribute('data-anime-id');
    var form = document.getElementById('edit-comment-form-' + commentId); // Utilisez l'ID du formulaire spécifique

    $(form).dialog({
        modal: true,
        width: '400px',
        buttons: [
            {
                text: "Save",
                click: function () {
                    updateComment(form, animeId, commentId);
                    $(this).dialog("close");
                }
            },
            {
                text: "Cancel",
                click: function () {
                    $(this).dialog("close");
                }
            }
        ],
        create: function () {
            $('.ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget');
            $('.ui-dialog-titlebar-close').html('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>');
        },
        open: function () {
            $('.ui-widget-overlay').addClass('custom-overlay');
        },
        close: function () {
            $('.ui-widget-overlay').removeClass('custom-overlay');
        }
    });

}

function updateComment(form, animeId, commentId) {
    fetch(`/api/animes/${animeId}/comments/${commentId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: form.elements['content'].value }),
        credentials: 'same-origin' // Ajout de cette ligne
    })
        .then(response => {
            if (response.ok) {
                var contentElement = document.querySelector('.comment[data-comment-id="' + commentId + '"] .comment-content');
                contentElement.innerText = form.elements['content'].value;
                $(form).dialog('close');
            } else {
                console.error('Failed to edit comment');
            }
        })
        .catch(error => {
            console.error(error);
        });
}





function deleteComment(element) {
    var commentId = element.getAttribute('data-comment-id');
    var animeId = element.getAttribute('data-anime-id');

    fetch(`/api/animes/${animeId}/comments/${commentId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => {
            if (response.ok) {
                // Supprimer l'affichage du commentaire
                var commentElement = document.querySelector('.comment[data-comment-id="' + commentId + '"]');
                commentElement.parentNode.removeChild(commentElement);
                console.log('Comment deleted');
            } else {
                console.error('Failed to delete comment');
            }
        })
        .catch(error => {
            console.error(error);
        });
}
