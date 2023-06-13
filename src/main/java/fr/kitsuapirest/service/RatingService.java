package fr.kitsuapirest.service;

import fr.kitsuapirest.exception.NotFoundException;
import fr.kitsuapirest.exception.UnauthorizedException;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Rating;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.repository.AnimeRepository;
import fr.kitsuapirest.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The RatingService class handles operations related to anime ratings.
 */
@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final AnimeService animeService;
    private final UserService userService;
    private final AnimeRepository animeRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, AnimeService animeService, UserService userService, AnimeRepository animeRepository) {
        this.ratingRepository = ratingRepository;
        this.animeService = animeService;
        this.userService = userService;
        this.animeRepository = animeRepository;
    }

    /**
     * Retrieves ratings for an anime by its ID.
     *
     * @param animeId the ID of the anime.
     * @return a list of ratings for the anime.
     */
    public List<Rating> getRatingsByAnimeId(Long animeId) {
        return ratingRepository.findByAnimeId(animeId);
    }

    /**
     * Adds a rating for an anime.
     *
     * @param animeId   the ID of the anime.
     * @param rating    the rating to add.
     * @param principal the principal object representing the current user.
     * @return the added rating.
     */
    public Rating addRating(Long animeId, Rating rating, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        Anime anime = animeService.getAnimeById(animeId);

        Rating existingRating = ratingRepository.findByUserAnimeKey(user.getId() + "_" + animeId);

        Rating finalRating = Optional.ofNullable(existingRating)
                .map(r -> {
                    r.setRating(rating.getRating()); // update the rating
                    return r;
                })
                .orElseGet(() -> { // otherwise, create a new rating
                    rating.setAnime(anime);
                    rating.setUser(user);
                    rating.setUserAnimeKey(user.getId() + "_" + animeId);
                    return rating;
                });

        return ratingRepository.save(finalRating);
    }

    /**
     * Retrieves a rating for an anime by the user and anime IDs.
     *
     * @param username the username of the user.
     * @param animeId  the ID of the anime.
     * @return the rating for the user and anime.
     */
    public Rating getRatingByUserAndAnime(String username, Long animeId) {
        User user = userService.getUserByUsername(username);
        Integer userId = user.getId();
        String userAnimeKey = userId + "_" + animeId;
        return ratingRepository.findByUserAnimeKey(userAnimeKey);
    }

    /**
     * Updates a rating for an anime.
     *
     * @param animeId      the ID of the anime.
     * @param ratingId     the ID of the rating.
     * @param updatedRating the updated rating.
     * @param userId       the ID of the user.
     * @return the updated rating.
     */
    public Rating updateRating(Long animeId, Long ratingId, Rating updatedRating, Long userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Rating not found"));

        if (!rating.getAnime().getId().equals(animeId) || !rating.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized access to rating");
        }

        rating.setRating(updatedRating.getRating());

        return ratingRepository.save(rating);
    }

    /**
     * Deletes a rating for an anime.
     *
     * @param animeId  the ID of the anime.
     * @param ratingId the ID of the rating.
     * @param userId   the ID of the user.
     */
    public void deleteRating(Long animeId, Long ratingId, Long userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Rating not found"));

        if (!rating.getAnime().getId().equals(animeId) || !rating.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized access to rating");
        }

        ratingRepository.delete(rating);
    }

    /**
     * Retrieves the top rated animes for a user.
     *
     * @param principal the principal object representing the current user.
     * @return a list of top rated animes for the user.
     */
    public List<Anime> getTopRatedAnimes(Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        Set<Rating> userRatings = user.getRatings();

        List<Rating> sortedRatings = userRatings.stream()
                .sorted(Comparator.comparingInt(Rating::getRating).reversed())
                .collect(Collectors.toList());

        List<Rating> top5Ratings = sortedRatings.stream()
                .limit(5)
                .collect(Collectors.toList());

        List<Anime> topRatedAnimes = top5Ratings.stream()
                .map(Rating::getAnime)
                .collect(Collectors.toList());

        return topRatedAnimes;
    }

    /**
     * Calculates the average rating for an anime by its ID.
     *
     * @param animeId the ID of the anime.
     * @return the average rating for the anime.
     */
    public double getAverageRatingByAnimeId(Long animeId) {
        List<Rating> ratings = ratingRepository.findByAnimeId(animeId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }

        double average = sum / ratings.size();
        average = Math.round(average * 100.0) / 100.0;

        return average;
    }
}
