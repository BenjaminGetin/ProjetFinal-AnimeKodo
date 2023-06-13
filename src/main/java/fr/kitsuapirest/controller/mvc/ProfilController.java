package fr.kitsuapirest.controller.mvc;

import fr.kitsuapirest.exception.NotFoundException;
import fr.kitsuapirest.exception.UnauthorizedException;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Comment;
import fr.kitsuapirest.model.Role;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * The ProfilController class handles the MVC endpoints for user profiles.
 */
@Controller
public class ProfilController {

    private final UserService userService;
    private final RatingService ratingService;
    private final CommentService commentService;
    private final AnimeService animeService;
    private final KitsuApiService kitsuApiService;

    public ProfilController(UserService userService, RatingService ratingService, CommentService commentService, AnimeService animeService, KitsuApiService kitsuApiService) {
        this.userService = userService;
        this.ratingService = ratingService;
        this.commentService = commentService;
        this.animeService = animeService;
        this.kitsuApiService = kitsuApiService;
    }

    /**
     * Displays the user profile page.
     *
     * @param model     the Model object to populate data for the view.
     * @param principal the authenticated principal representing the user.
     * @return the name of the view template for the user profile page.
     */
    @GetMapping("/profil")
    public String showProfil(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        List<Anime> topRatedAnimes = ratingService.getTopRatedAnimes(principal);
        List<Comment> comments = commentService.getCommentsByUserId(username);

        model.addAttribute("user", user);
        model.addAttribute("topRatedAnimes", topRatedAnimes);
        model.addAttribute("comments", comments);

        return "profil";
    }

    /**
     * Performs a search for an anime by title.
     *
     * @param principal the authenticated principal representing the user.
     * @param title     the title of the anime to search for.
     * @param model     the Model object to populate data for the view.
     * @return the name of the view template for the user profile page.
     */
    @PostMapping("/profil/search")
    public String searchAnime(Principal principal, @RequestParam("animeSearch") String title, Model model) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        List<Anime> searchResults = kitsuApiService.searchAnimeByTitle(title, user.getRole().toString());
        Anime firstResult = searchResults.isEmpty() ? null : searchResults.get(0);

        model.addAttribute("firstResult", firstResult);
        model.addAttribute("user", user);

        return "profil";
    }

    /**
     * Adds an anime to the database.
     *
     * @param principal  the authenticated principal representing the user.
     * @param animeTitle the title of the anime to add.
     * @param model      the Model object to populate data for the view.
     * @return the name of the view template for the user profile page.
     * @throws UnauthorizedException if the user is not an administrator.
     * @throws NotFoundException     if the anime is not found.
     */
    @PostMapping("/profil/addAnime")
    public String addAnime(Principal principal, @RequestParam("animeTitle") String animeTitle, Model model) {
        // Check if the user is an administrator
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Unauthorized access");
        }

        // Search for the anime in the search results
        // Search for the anime in the API
        List<Anime> searchResults = kitsuApiService.searchAnimeByTitle(animeTitle, user.getRole().toString());
        Anime anime = searchResults.isEmpty() ? null : searchResults.get(0);

        if (anime == null) {
            throw new NotFoundException("Anime not found");
        }

        // Add the anime to the database
        kitsuApiService.addAnime(anime, user.getRole().toString());

        // Add the search results to the model
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("user", user);

        // Get other model attributes and return the view as in the showProfil method
        // ...

        return "profil";
    }
}
