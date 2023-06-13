package fr.kitsuapirest.controller.mvc;

import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.service.UserService;
import fr.kitsuapirest.service.WatchlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

/**
 * The WatchListController class handles the MVC endpoint for displaying the user's watchlist.
 */
@Controller
public class WatchListController {

    private final UserService userService;
    private final WatchlistService watchlistService;

    public WatchListController(UserService userService, WatchlistService watchlistService) {
        this.userService = userService;
        this.watchlistService = watchlistService;
    }

    /**
     * Shows the user's watchlist.
     *
     * @param model     the Model object to populate data for the view.
     * @param principal the authenticated principal representing the user.
     * @return the name of the view template for the watchlist page.
     */
    @GetMapping("/watchlist")
    public String showWatchlist(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User authenticatedUser = userService.getUserByUsername(username);
            model.addAttribute("user", authenticatedUser);

            // Retrieve the user's watchlist and add it to the model.
            List<Anime> watchlist = watchlistService.getWatchlistByUserId(authenticatedUser.getId());
            model.addAttribute("animes", watchlist);
        }

        return "watchlist";
    }
}
