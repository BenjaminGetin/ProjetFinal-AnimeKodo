package fr.kitsuapirest.controller.mvc;

import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.service.UserService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * The SearchController class handles the MVC endpoint for searching anime.
 */
@Controller
public class SearchController {

    private final RestTemplate restTemplate;
    private final UserService userService;

    public SearchController(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    /**
     * Performs a search for anime based on the provided parameters.
     *
     * @param model    the Model object to populate data for the view.
     * @param allParams a map of all query parameters.
     * @param principal the authenticated principal representing the user.
     * @return the name of the view template for the search results page.
     */
    @GetMapping("/anime")
    public String search(Model model, @RequestParam Map<String, String> allParams, Principal principal) {
        String apiUrl = "http://localhost:8080/api/animes";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);

        allParams.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                builder.queryParam(key, value);
            }
        });

        ResponseEntity<List<Anime>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                });

        List<Anime> animeList = response.getBody();
        model.addAttribute("animes", animeList);

        if (principal != null) {
            String username = principal.getName();
            User authenticatedUser = userService.getUserByUsername(username);
            model.addAttribute("user", authenticatedUser);
        }

        return "animes";
    }
}
