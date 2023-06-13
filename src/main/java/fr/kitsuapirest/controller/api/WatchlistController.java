package fr.kitsuapirest.controller.api;

import fr.kitsuapirest.exception.UnauthorizedException;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Watchlist;
import fr.kitsuapirest.service.UserService;
import fr.kitsuapirest.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The WatchlistController class handles API endpoints related to watchlists for users.
 */
@RestController
@RequestMapping("api/users/{userId}/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private UserService userService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService, UserService userService) {
        this.watchlistService = watchlistService;
        this.userService = userService;
    }

    /**
     * Retrieves the watchlist for a specific user.
     *
     * @param userId the ID of the user.
     * @return a list of Anime objects representing the user's watchlist.
     */
    @GetMapping()
    public List<Anime> getWatchlistByUserId(@PathVariable Integer userId) {
        return watchlistService.getWatchlistByUserId(userId);
    }

    /**
     * Adds an anime to the user's watchlist.
     *
     * @param animeId   the ID of the anime to add to the watchlist.
     * @param principal the authenticated principal representing the user.
     * @return the updated Watchlist object after adding the anime to the watchlist.
     * @throws UnauthorizedException if the principal is null, indicating unauthorized access.
     */
    @PostMapping("/add-anime/{animeId}")
    public Watchlist addToWatchlist(@PathVariable Long animeId, Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        return watchlistService.addToWatchlist(animeId, principal);
    }

    /**
     * Removes an anime from the user's watchlist.
     *
     * @param animeId   the ID of the anime to remove from the watchlist.
     * @param principal the authenticated principal representing the user.
     * @return the updated Watchlist object after removing the anime from the watchlist.
     */
    @DeleteMapping("/remove-anime/{animeId}")
    public Watchlist removeFromWatchlist(@PathVariable Long animeId, Principal principal) {
        return watchlistService.removeFromWatchlist(animeId, principal);
    }

    /**
     * Checks if an anime is present in the user's watchlist.
     *
     * @param animeId   the ID of the anime to check.
     * @param principal the authenticated principal representing the user.
     * @return a ResponseEntity containing a map with a boolean indicating if the anime is in the watchlist.
     */
    @GetMapping("/is-in-watchlist/{animeId}")
    public ResponseEntity<Map<String, Boolean>> isInWatchlist(@PathVariable Long animeId, Principal principal) {
        boolean isInWatchlist = watchlistService.isInWatchlist(animeId, principal);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isInWatchlist", isInWatchlist);
        return ResponseEntity.ok(response);
    }
}
