package fr.kitsuapirest.controller;

import fr.kitsuapirest.entity.WatchlistEntity;
import fr.kitsuapirest.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping("/add-anime/{animeId}")
    public WatchlistEntity addToWatchlist(@PathVariable Long userId, @PathVariable Long animeId) {
        return watchlistService.addToWatchlist(userId, animeId);
    }

    @PostMapping("/remove-anime/{animeId}")
    public WatchlistEntity removeFromWatchlist(@PathVariable Long userId, @PathVariable Long animeId) {
        return watchlistService.removeFromWatchlist(userId, animeId);
    }
}
