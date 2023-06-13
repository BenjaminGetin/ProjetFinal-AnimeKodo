package fr.kitsuapirest.controller.api;

import fr.kitsuapirest.ApiResponse.AnimeData;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.service.AnimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * The AnimeController class handles API endpoints related to anime.
 */
@RestController
@RequestMapping("api/animes")
public class AnimeController {

    private final AnimeService animeService;

    /**
     * Constructs a new AnimeController with the given AnimeService.
     *
     * @param animeService the AnimeService to be used for anime-related operations.
     */
    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    /**
     * Retrieves all animes based on the provided parameters.
     *
     * @param parameters the parameters for filtering the anime list.
     * @return a ResponseEntity containing a list of Anime objects if successful, or an empty list if no animes are found.
     */
    @GetMapping()
    public ResponseEntity<List<Anime>> getAllAnimes(@RequestParam Map<String, String> parameters) {
        List<Anime> animes = animeService.getAllAnimes(parameters);
        return ResponseEntity.ok(animes);
    }

    /**
     * Retrieves the trending animes.
     *
     * @return a list of AnimeData objects representing the trending animes.
     */
    @GetMapping("/trending")
    public List<AnimeData> getTrendingAnimes() {
        return animeService.getTrendingAnimes();
    }

    /**
     * Retrieves the upcoming animes.
     *
     * @return a list of AnimeData objects representing the upcoming animes.
     */
    @GetMapping("/upcoming")
    public List<AnimeData> getUpcomingAnimes() {
        return animeService.getUpcomingAnimes();
    }

    /**
     * Retrieves an anime by its ID.
     *
     * @param id the ID of the anime to retrieve.
     * @return a ResponseEntity containing the Anime object if found, or ResponseEntity.notFound() if the anime is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Anime> getAnimeById(@PathVariable Long id) {
        Anime anime = animeService.getAnimeById(id);
        if (anime != null) {
            return ResponseEntity.ok(anime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an anime by its ID.
     *
     * @param id the ID of the anime to delete.
     * @return a ResponseEntity indicating a successful deletion with ResponseEntity.noContent().
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnime(@PathVariable Long id) {
        animeService.deleteAnime(id);
        return ResponseEntity.noContent().build();
    }
}
