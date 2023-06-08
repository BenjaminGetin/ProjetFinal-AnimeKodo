package fr.kitsuapirest.controller;

import fr.kitsuapirest.entity.RatingEntity;
import fr.kitsuapirest.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animes/{animeId}/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public List<RatingEntity> getRatingsByAnimeId(@PathVariable Long animeId) {
        return ratingService.getRatingsByAnimeId(animeId);
    }

    @PostMapping
    public RatingEntity addRating(@PathVariable Long animeId, @RequestBody RatingEntity rating) {
        return ratingService.addRating(animeId, rating);
    }

    @GetMapping("/user/{userId}")
    public RatingEntity getRatingByUserAndAnime(@PathVariable Long animeId, @PathVariable Long userId) {
        return ratingService.getRatingByUserAndAnime(userId, animeId);
    }

    @PutMapping("/{ratingId}")
    public RatingEntity updateRating(@PathVariable Long animeId, @PathVariable Long ratingId, @RequestBody RatingEntity rating, @PathVariable Long userId) {
        return ratingService.updateRating(animeId, ratingId, rating, userId);
    }

    @DeleteMapping("/{ratingId}")
    public void deleteRating(@PathVariable Long animeId, @PathVariable Long ratingId, @PathVariable Long userId) {
        ratingService.deleteRating(animeId, ratingId, userId);
    }

}
