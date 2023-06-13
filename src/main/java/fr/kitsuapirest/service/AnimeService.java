package fr.kitsuapirest.service;

import fr.kitsuapirest.ApiResponse.AnimeData;
import fr.kitsuapirest.ApiResponse.ApiResponse;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Rating;
import fr.kitsuapirest.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The AnimeService class handles operations related to anime data.
 */
@Service
public class AnimeService {

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves a list of trending animes.
     *
     * @return a list of trending AnimeData.
     */
    public List<AnimeData> getTrendingAnimes() {
        String trendingAnimesUrl = "https://kitsu.io/api/edge/anime?filter[seasonYear]=2023&filter[season]=spring&page[limit]=9&sort=-averageRating";
        ApiResponse response = restTemplate.getForObject(trendingAnimesUrl, ApiResponse.class);
        return response.getData();
    }

    /**
     * Retrieves a list of upcoming animes.
     *
     * @return a list of upcoming AnimeData.
     */
    public List<AnimeData> getUpcomingAnimes() {
        String upcomingAnimesUrl = "https://kitsu.io/api/edge/anime?filter[seasonYear]=2023&filter[season]=summer&page[limit]=9&sort=-averageRating";
        ApiResponse response = restTemplate.getForObject(upcomingAnimesUrl, ApiResponse.class);
        return response.getData();
    }

    /**
     * Retrieves a list of all animes with optional filtering and sorting.
     *
     * @param parameters the query parameters for filtering and sorting.
     * @return a list of filtered and sorted Anime objects.
     */
    public List<Anime> getAllAnimes(Map<String, String> parameters) {
        List<Anime> animes = animeRepository.findAll();

        String title = parameters.get("title");
        if (title != null && !title.isEmpty()) {
            animes = animes.stream()
                    .filter(anime -> anime.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        }

        String subtype = parameters.get("subtype");
        if (subtype != null && !subtype.isEmpty()) {
            animes = animes.stream()
                    .filter(anime -> anime.getSubtype().equalsIgnoreCase(subtype))
                    .collect(Collectors.toList());
        }

        String sortBy = parameters.get("sortBy");
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equals("oldest")) {
                Collections.sort(animes, Comparator.comparing(Anime::getStartDate));
            } else if (sortBy.equals("newest")) {
                Collections.sort(animes, Comparator.comparing(Anime::getStartDate).reversed());
            } else if (sortBy.equals("lowest")) {
                Collections.sort(animes, Comparator.comparingInt(Anime::getEpisodeCount));
            } else if (sortBy.equals("highest")) {
                Collections.sort(animes, Comparator.comparingInt(Anime::getEpisodeCount).reversed());
            }
        }

        String status = parameters.get("status");
        if (status != null && !status.isEmpty()) {
            animes = animes.stream()
                    .filter(anime -> anime.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        return animes;
    }

    /**
     * Retrieves an anime by its ID.
     *
     * @param id the ID of the anime.
     * @return the Anime object, or null if not found.
     */
    public Anime getAnimeById(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    /**
     * Deletes an anime by its ID.
     *
     * @param id the ID of the anime to delete.
     */
    public void deleteAnime(Long id) {
        animeRepository.deleteById(id);
    }

    /**
     * Retrieves an anime by its title.
     *
     * @param anime the title of the anime.
     * @return the Anime object, or null if not found.
     */
    public Anime getAnimeByTitle(String anime) {
        return animeRepository.findByTitle(anime);
    }
}
