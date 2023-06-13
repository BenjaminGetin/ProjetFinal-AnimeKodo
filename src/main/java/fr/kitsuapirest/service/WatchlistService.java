package fr.kitsuapirest.service;

import fr.kitsuapirest.exception.NotFoundException;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.model.Watchlist;
import fr.kitsuapirest.repository.AnimeRepository;
import fr.kitsuapirest.repository.UserRepository;
import fr.kitsuapirest.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The WatchlistService class handles operations related to watchlists.
 */
@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final AnimeRepository animeRepository;
    private final UserService userService;

    @Autowired
    public WatchlistService(WatchlistRepository watchlistRepository, UserRepository userRepository,
                            AnimeRepository animeRepository, UserService userService) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.animeRepository = animeRepository;
        this.userService = userService;
    }

    /**
     * Adds an anime to the user's watchlist.
     *
     * @param animeId   the ID of the anime.
     * @param principal the authenticated user.
     * @return the updated watchlist.
     * @throws NotFoundException        if the anime or watchlist is not found.
     * @throws IllegalArgumentException if the anime already exists in the watchlist.
     */
    public Watchlist addToWatchlist(Long animeId, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new NotFoundException("Anime not found"));

        Optional<Watchlist> optionalWatchlist = watchlistRepository.findByUser_Id(user.getId());
        Watchlist watchlist;
        if (optionalWatchlist.isPresent()) {
            watchlist = optionalWatchlist.get();
            Set<Anime> animes = watchlist.getAnimes();
            if (animes.contains(anime)) {
                throw new IllegalArgumentException("Anime already exists in the watchlist");
            }
            animes.add(anime);
        } else {
            watchlist = new Watchlist(user);
            watchlist.getAnimes().add(anime);
        }

        return watchlistRepository.save(watchlist);
    }

    /**
     * Removes an anime from the user's watchlist.
     *
     * @param animeId   the ID of the anime.
     * @param principal the authenticated user.
     * @return the updated watchlist.
     * @throws NotFoundException        if the anime or watchlist is not found.
     * @throws IllegalArgumentException if the anime does not exist in the watchlist.
     */
    public Watchlist removeFromWatchlist(Long animeId, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new NotFoundException("Anime not found"));

        Optional<Watchlist> optionalWatchlist = watchlistRepository.findByUser_Id(user.getId());
        Watchlist watchlist = optionalWatchlist.orElseThrow(() -> new NotFoundException("Watchlist not found"));

        Set<Anime> animes = watchlist.getAnimes();
        if (!animes.contains(anime)) {
            throw new IllegalArgumentException("Anime does not exist in the watchlist");
        }
        animes.remove(anime);

        return watchlistRepository.save(watchlist);
    }

    /**
     * Retrieves the watchlist of a user by user ID.
     *
     * @param userId the ID of the user.
     * @return the list of animes in the watchlist.
     */
    public List<Anime> getWatchlistByUserId(Integer userId) {
        List<Watchlist> watchlists = watchlistRepository.findByUserId(userId);
        List<Anime> animes = new ArrayList<>();
        for (Watchlist watchlist : watchlists) {
            animes.addAll(watchlist.getAnimes()); // This assumes that Watchlist has a method getAnimes() that returns List<Anime>
        }
        return animes;
    }

    /**
     * Checks if an anime is in the user's watchlist.
     *
     * @param animeId   the ID of the anime.
     * @param principal the authenticated user.
     * @return true if the anime is in the watchlist, false otherwise.
     */
    public boolean isInWatchlist(Long animeId, Principal principal) {
        if (principal == null) {
            return false;
        }

        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        Optional<Watchlist> optionalWatchlist = watchlistRepository.findByUser_Id(user.getId());
        if (optionalWatchlist.isPresent()) {
            Watchlist watchlist = optionalWatchlist.get();
            Set<Anime> animes = watchlist.getAnimes();
            return animes.stream().anyMatch(anime -> anime.getId().equals(animeId));
        }
        return false;
    }
}
