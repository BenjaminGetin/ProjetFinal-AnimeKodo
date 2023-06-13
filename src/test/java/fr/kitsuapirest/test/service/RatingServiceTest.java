package fr.kitsuapirest.test.service;

import fr.kitsuapirest.exception.NotFoundException;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Rating;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.repository.AnimeRepository;
import fr.kitsuapirest.repository.RatingRepository;
import fr.kitsuapirest.service.AnimeService;
import fr.kitsuapirest.service.RatingService;
import fr.kitsuapirest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private AnimeService animeService;

    @Mock
    private UserService userService;

    @Mock
    private AnimeRepository animeRepository;

    @InjectMocks
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetRatingsByAnimeId() {
        Long animeId = 1L;
        List<Rating> expectedRatings = new ArrayList<>();
        expectedRatings.add(new Rating(4.5));
        when(ratingRepository.findByAnimeId(animeId)).thenReturn(expectedRatings);

        List<Rating> actualRatings = ratingService.getRatingsByAnimeId(animeId);

        assertEquals(expectedRatings, actualRatings);
        verify(ratingRepository, times(1)).findByAnimeId(animeId);
    }

    @Test
    void testAddRating() {
        Long animeId = 1L;
        Rating rating = new Rating(4.5);
        rating.setRating((int) 4.5);
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        User user = new User();
        user.setId(1);
        when(userService.getUserByUsername("username")).thenReturn(user);

        Anime anime = new Anime();
        when(animeService.getAnimeById(animeId)).thenReturn(anime);

        when(ratingRepository.findByUserAnimeKey(anyString())).thenReturn(null);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating addedRating = ratingService.addRating(animeId, rating, principal);

        assertNotNull(addedRating);
        assertEquals(rating.getRating(), addedRating.getRating());
        verify(userService, times(1)).getUserByUsername("username");
        verify(animeService, times(1)).getAnimeById(animeId);
        verify(ratingRepository, times(1)).findByUserAnimeKey(anyString());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testGetRatingByUserAndAnime() {
        String username = "username";
        Long animeId = 1L;
        User user = new User();
        user.setId(1);
        when(userService.getUserByUsername(username)).thenReturn(user);

        String userAnimeKey = "1_1";
        Rating expectedRating = new Rating(4.5);
        when(ratingRepository.findByUserAnimeKey(userAnimeKey)).thenReturn(expectedRating);

        Rating actualRating = ratingService.getRatingByUserAndAnime(username, animeId);

        assertEquals(expectedRating, actualRating);
        verify(userService, times(1)).getUserByUsername(username);
        verify(ratingRepository, times(1)).findByUserAnimeKey(userAnimeKey);
    }


    @Test
    void testUpdateRating_ThrowsNotFoundException() {
        Long animeId = 1L;
        Long ratingId = 1L;
        Long userId = 1L;
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.empty());

        Rating updatedRating = new Rating(4.5);
        updatedRating.setRating((int) 4.5);

        assertThrows(NotFoundException.class, () ->
                ratingService.updateRating(animeId, ratingId, updatedRating, userId));

        verify(ratingRepository, times(1)).findById(ratingId);
        verify(ratingRepository, times(0)).save(any(Rating.class));
    }


    @Test
    void testGetAverageRatingByAnimeId_NoRatings() {
        Long animeId = 1L;
        when(ratingRepository.findByAnimeId(animeId)).thenReturn(Collections.emptyList());

        double averageRating = ratingService.getAverageRatingByAnimeId(animeId);

        assertEquals(0.0, averageRating);
        verify(ratingRepository, times(1)).findByAnimeId(animeId);
    }
}
