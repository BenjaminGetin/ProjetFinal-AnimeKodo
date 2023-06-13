package fr.kitsuapirest.repository;

import fr.kitsuapirest.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Anime findByTitle(String title);
}


