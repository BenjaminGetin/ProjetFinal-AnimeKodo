package fr.kitsuapirest.controller.api;

import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Rating;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.service.RatingService;
import fr.kitsuapirest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * The RatingController class handles API endpoints related to ratings for anime.
 */
@RestController
@RequestMapping("api/animes/{animeId}/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final UserService userService;

    @Autowired
    public RatingController(RatingService ratingService, UserService userService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }

    /**
     * Retrieves all ratings for a specific anime.
     *
     * @param animeId the ID of the anime.
     * @return a list of Rating objects associated with the anime.
     */
    @GetMapping
    public List<Rating> getRatingsByAnimeId(@PathVariable Long animeId) {
        return ratingService.getRatingsByAnimeId(animeId);
    }

    /**
     * Retrieves the rating given by a specific user for a specific anime.
     *
     * @param animeId the ID of the anime.
     * @param userId  the ID of the user.
     * @return the Rating object associated with the user and anime, or null if no rating is found.
     */
    @GetMapping("/user/{userId}")
    public Rating getRatingByUserAndAnime(@PathVariable Long animeId, @PathVariable String userId) {
        return ratingService.getRatingByUserAndAnime(userId, animeId);
    }

    /**
     * Updates an existing rating for a specific anime.
     *
     * @param animeId  the ID of the anime.
     * @param ratingId the ID of the rating to update.
     * @param rating   the updated Rating object.
     * @param userId   the ID of the user.
     * @return the updated Rating object.
     */
    @PutMapping("/{ratingId}")
    public Rating updateRating(@PathVariable Long animeId, @PathVariable Long ratingId, @RequestBody Rating rating, @PathVariable Long userId) {
        return ratingService.updateRating(animeId, ratingId, rating, userId);
    }

    /**
     * Deletes a rating for a specific anime.
     *
     * @param animeId  the ID of the anime.
     * @param ratingId the ID of the rating to delete.
     * @param userId   the ID of the user.
     */
    @DeleteMapping("/{ratingId}")
    public void deleteRating(@PathVariable Long animeId, @PathVariable Long ratingId, @PathVariable Long userId) {
        ratingService.deleteRating(animeId, ratingId, userId);
    }
}
