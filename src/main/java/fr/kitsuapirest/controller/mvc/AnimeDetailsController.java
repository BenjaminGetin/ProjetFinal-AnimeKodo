package fr.kitsuapirest.controller.mvc;

import fr.kitsuapirest.dto.CommentForm;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Comment;
import fr.kitsuapirest.model.Rating;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.service.AnimeService;
import fr.kitsuapirest.service.CommentService;
import fr.kitsuapirest.service.RatingService;
import fr.kitsuapirest.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The AnimeDetailsController class handles the MVC endpoints for anime details.
 */
@Controller
public class AnimeDetailsController {

    private final AnimeService animeService;
    private final UserService userService;
    private final CommentService commentService;
    private final RatingService ratingService;

    public AnimeDetailsController(AnimeService animeService, UserService userService, CommentService commentService, RatingService ratingService) {
        this.animeService = animeService;
        this.userService = userService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    /**
     * Retrieves the details page for a specific anime.
     *
     * @param id        the ID of the anime.
     * @param model     the Model object to populate data for the view.
     * @param principal the authenticated principal representing the user.
     * @return the name of the view template for the anime details page.
     * @throws AnimeNotFoundException if the anime is not found.
     */
    @GetMapping("/animes/{id}")
    public String getAnimeDetails(@PathVariable Long id, Model model, Principal principal) {
        Anime anime = animeService.getAnimeById(id);

        Integer userId = null;
        String username = null;
        String principalName = null;
        double averageRating = ratingService.getAverageRatingByAnimeId(id);

        if (principal != null) {
            username = principal.getName();
            principalName = principal.getName();
            userId = userService.getUserIdByUsername(username);
            User authenticatedUser = userService.getUserByUsername(username);
            model.addAttribute("user", authenticatedUser);

            Rating userRating = ratingService.getRatingByUserAndAnime(username, id);
            model.addAttribute("userRating", userRating);
        }

        List<Comment> comments = commentService.getCommentsByAnimeId(id);

        model.addAttribute("userId", userId);
        model.addAttribute("anime", anime);
        model.addAttribute("comments", comments);
        model.addAttribute("username", username);
        model.addAttribute("principal", principalName);
        model.addAttribute("averageRating", averageRating);

        if (anime == null) {
            throw new AnimeNotFoundException("Anime not found");
        }

        return "anime-details";
    }

    /**
     * Adds a comment to a specific anime.
     *
     * @param animeId     the ID of the anime.
     * @param commentForm the CommentForm object containing the comment details.
     * @param principal   the authenticated principal representing the user.
     * @return a redirect URL to the anime details page.
     */
    @PostMapping("/animes/{id}/comments")
    public String addComment(@PathVariable("id") Long animeId, @ModelAttribute("commentForm") CommentForm commentForm, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Anime anime = animeService.getAnimeById(animeId);

        Comment comment = new Comment();
        comment.setContent(commentForm.getContent());
        comment.setUser(user);
        comment.setAnime(anime);
        comment.setCreatedAt(LocalDateTime.now());
        Comment createdComment = commentService.addComment(animeId, comment, principal);

        if (createdComment != null) {
            return "redirect:/animes/" + animeId;
        } else {
            return "redirect:/animes/" + animeId;
        }
    }

    /**
     * Adds a rating to a specific anime.
     *
     * @param animeId        the ID of the anime.
     * @param selectedRating the selected rating value.
     * @param principal      the authenticated principal representing the user.
     * @return a redirect URL to the anime details page.
     */
    @PostMapping("/animes/{id}/ratings")
    public String addRating(@PathVariable("id") Long animeId, @RequestParam("selectedRating") int selectedRating, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect to the login page
        }

        User user = userService.getUserByUsername(principal.getName());
        Anime anime = animeService.getAnimeById(animeId);

        Rating rating = new Rating(4.5); // Set a default rating value
        rating.setRating(selectedRating);
        rating.setUser(user);
        rating.setAnime(anime);

        Rating createdRating = ratingService.addRating(animeId, rating, principal);

        if (createdRating != null) {
            return "redirect:/animes/" + animeId;
        } else {
            return "redirect:/animes/" + animeId;
        }
    }

    /**
     * Custom exception class for when an anime is not found.
     */
    public class AnimeNotFoundException extends RuntimeException {
        public AnimeNotFoundException(String message) {
            super(message);
        }
    }
}
