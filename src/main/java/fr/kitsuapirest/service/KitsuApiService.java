package fr.kitsuapirest.service;

import fr.kitsuapirest.ApiResponse.AnimeAttributes;
import fr.kitsuapirest.ApiResponse.AnimeData;
import fr.kitsuapirest.ApiResponse.ApiResponse;
import fr.kitsuapirest.exception.UnauthorizedException;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Role;
import fr.kitsuapirest.repository.AnimeRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The KitsuApiService class handles operations related to the Kitsu API.
 */
@Service
public class KitsuApiService {

    private final RestTemplate restTemplate;
    private final AnimeRepository animeRepository;

    public KitsuApiService(RestTemplate restTemplate, AnimeRepository animeRepository) {
        this.restTemplate = restTemplate;
        this.animeRepository = animeRepository;
    }

    /**
     * Searches for an anime by title using the Kitsu API.
     *
     * @param title the title of the anime to search.
     * @param role  the role of the user performing the search.
     * @return a list of found anime.
     * @throws UnauthorizedException if the user is not authorized to access the search results.
     */
    public List<Anime> searchAnimeByTitle(String title, String role) {
        String apiUrl = "https://kitsu.io/api/edge/anime?filter[text]=" + title;

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                apiUrl, HttpMethod.GET, null, ApiResponse.class);
        ApiResponse apiResponse = response.getBody();

        if (!role.equals("ADMIN")) {
            throw new UnauthorizedException("Unauthorized access");
        }

        if (apiResponse != null && apiResponse.getData() != null) {
            List<AnimeData> animeDataList = apiResponse.getData();
            List<Anime> searchedAnimes = new ArrayList<>();

            for (AnimeData animeData : animeDataList) {
                AnimeAttributes animeAttributes = animeData.getAttributes();
                Anime anime = new Anime();

                anime.setTitle(animeAttributes.getTitles().getEn());
                anime.setImage(animeAttributes.getPosterImage().getOriginal());
                anime.setSynopsis(animeAttributes.getSynopsis());
                anime.setStartDate(animeAttributes.getStartDate());
                anime.setEndDate(animeAttributes.getEndDate());
                anime.setEpisodeCount(animeAttributes.getEpisodeCount());
                anime.setEpisodeLength(animeAttributes.getEpisodeLength());
                anime.setSubtype(animeAttributes.getSubtype());
                anime.setStatus(animeAttributes.getStatus());
                anime.setAgeRatingGuide(animeAttributes.getAgeRatingGuide());

                searchedAnimes.add(anime);
            }

            return searchedAnimes;
        }

        return Collections.emptyList();
    }

    /**
     * Adds an anime to the database.
     *
     * @param anime the anime to add.
     * @param role  the role of the user performing the operation.
     * @return the added anime.
     * @throws UnauthorizedException       if the user is not authorized to add an anime.
     * @throws AnimeAlreadyExistsException if the anime already exists in the database.
     */
    public Anime addAnime(Anime anime, String role) {
        Anime existingAnime = animeRepository.findByTitle(anime.getTitle());

        if (existingAnime != null) {
            throw new AnimeAlreadyExistsException("The anime " + anime.getTitle() + " already exists in the database.");
        }

        if (!role.equals("ADMIN")) {
            throw new UnauthorizedException("Unauthorized access");
        }

        return animeRepository.save(anime);
    }

    /**
     * Custom exception class for handling anime already exists scenario.
     */
    public class AnimeAlreadyExistsException extends RuntimeException {
        public AnimeAlreadyExistsException(String message) {
            super(message);
        }
    }
}
