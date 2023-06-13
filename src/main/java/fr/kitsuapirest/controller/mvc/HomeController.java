package fr.kitsuapirest.controller.mvc;

import fr.kitsuapirest.ApiResponse.AnimeData;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.service.AnimeService;
import fr.kitsuapirest.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

/**
 * The HomeController class handles the MVC endpoints for the home page.
 */
@Controller
public class HomeController {

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final AnimeService animeService;

    public HomeController(RestTemplate restTemplate, UserService userService, AnimeService animeService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.animeService = animeService;
    }

    /**
     * Displays the home page.
     *
     * @param model     the Model object to populate data for the view.
     * @param principal the authenticated principal representing the user.
     * @return the name of the view template for the home page.
     */
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        String trendingApiUrl = "http://localhost:8080/api/animes/trending";
        AnimeData[] trendingAnimes = restTemplate.getForObject(trendingApiUrl, AnimeData[].class);

        String upcomingApiUrl = "http://localhost:8080/api/animes/upcoming";
        AnimeData[] upcomingAnimes = restTemplate.getForObject(upcomingApiUrl, AnimeData[].class);

        for (AnimeData animeData : trendingAnimes) {
            Anime anime = animeService.getAnimeByTitle(animeData.getAttributes().getTitles().getEn());
            if (anime != null) {
                animeData.setId(anime.getId().toString());
            }
        }

        for (AnimeData animeData : upcomingAnimes) {
            Anime anime = animeService.getAnimeByTitle(animeData.getAttributes().getTitles().getEn());
            if (anime != null) {
                animeData.setId(anime.getId().toString());
            }
        }

        model.addAttribute("trendingAnimes", trendingAnimes);
        model.addAttribute("upcomingAnimes", upcomingAnimes);

        if (principal != null) {
            String username = principal.getName();
            User authenticatedUser = userService.getUserByUsername(username);
            model.addAttribute("user", authenticatedUser);
        }

        return "home";
    }
}
