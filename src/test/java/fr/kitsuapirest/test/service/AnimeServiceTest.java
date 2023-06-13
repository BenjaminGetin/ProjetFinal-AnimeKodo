package fr.kitsuapirest.test.service;

import fr.kitsuapirest.ApiResponse.AnimeData;
import fr.kitsuapirest.ApiResponse.ApiResponse;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.repository.AnimeRepository;
import fr.kitsuapirest.service.AnimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnimeServiceTest {

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AnimeService animeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGetAllAnimes() {
        Anime anime1 = new Anime();
        anime1.setTitle("Anime 1");
        anime1.setSubtype("TV");
        anime1.setStartDate(LocalDate.ofEpochDay(2023-06-12));
        anime1.setEpisodeCount(12);
        anime1.setStatus("Finished");

        Anime anime2 = new Anime();
        anime2.setTitle("Anime 2");
        anime2.setSubtype("Movie");
        anime2.setStartDate(LocalDate.ofEpochDay(2023-02-01));
        anime2.setEpisodeCount(1);
        anime2.setStatus("Ongoing");

        List<Anime> allAnimes = Arrays.asList(anime1, anime2);

        when(animeRepository.findAll()).thenReturn(allAnimes);

        Map<String, String> parameters = Collections.emptyMap();

        List<Anime> result = animeService.getAllAnimes(parameters);

        assertEquals(2, result.size());
        assertEquals("Anime 1", result.get(0).getTitle());
        assertEquals("Anime 2", result.get(1).getTitle());
        verify(animeRepository, times(1)).findAll();
    }

    @Test
    void testGetAnimeById() {
        Long animeId = 1L;
        Anime anime = new Anime();
        anime.setTitle("Anime 1");

        when(animeRepository.findById(animeId)).thenReturn(java.util.Optional.of(anime));

        Anime result = animeService.getAnimeById(animeId);

        assertEquals(anime.getTitle(), result.getTitle());
        verify(animeRepository, times(1)).findById(animeId);
    }

    @Test
    void testDeleteAnime() {
        Long animeId = 1L;

        animeService.deleteAnime(animeId);

        verify(animeRepository, times(1)).deleteById(animeId);
    }

    @Test
    void testGetAnimeByTitle() {
        String title = "Anime 1";
        Anime anime = new Anime();
        anime.setTitle(title);

        when(animeRepository.findByTitle(title)).thenReturn(anime);

        Anime result = animeService.getAnimeByTitle(title);

        assertEquals(title, result.getTitle());
        verify(animeRepository, times(1)).findByTitle(title);
    }
}
