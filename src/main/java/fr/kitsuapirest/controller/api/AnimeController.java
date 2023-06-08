package fr.kitsuapirest.controller;

import fr.kitsuapirest.ApiResponse.AnimeData;
import fr.kitsuapirest.entity.AnimeEntity;
import fr.kitsuapirest.service.AnimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/animes")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping("/trending")
    public List<AnimeData> getTrendingAnimes() {
        return animeService.getTrendingAnimes();
    }

    @GetMapping("/upcoming")
    public List<AnimeData> getUpcomingAnimes() {
        return animeService.getUpcomingAnimes();
    }




    @GetMapping
    public ResponseEntity<List<AnimeEntity>> getAllAnimes(@RequestParam Map<String,String> parameters) {
        List<AnimeEntity> animes = animeService.getAllAnimes(parameters);
        return ResponseEntity.ok(animes);
    }




    @GetMapping("/{id}")
    public ResponseEntity<AnimeEntity> getAnimeById(@PathVariable Long id) {
        AnimeEntity anime = animeService.getAnimeById(id);
        if (anime != null) {
            return ResponseEntity.ok(anime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //@PostMapping
    //public ResponseEntity<AnimeEntity> createAnime(@RequestBody AnimeEntity newAnime) {
    //    AnimeEntity anime = animeService.saveAnime(newAnime);
    //    return ResponseEntity.status(HttpStatus.CREATED).body(anime);
    //}

    //@PutMapping("/{id}")
    //public ResponseEntity<AnimeEntity> updateAnime(@PathVariable Long id, @RequestBody AnimeEntity updatedAnime) {
    //    AnimeEntity anime = animeService.updateAnime(id, updatedAnime);
    //    if (anime != null) {
    //        return ResponseEntity.ok(anime);
    //    } else {
    //        return ResponseEntity.notFound().build();
    //    }
    //}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnime(@PathVariable Long id) {
        animeService.deleteAnime(id);
        return ResponseEntity.noContent().build();
    }
}

