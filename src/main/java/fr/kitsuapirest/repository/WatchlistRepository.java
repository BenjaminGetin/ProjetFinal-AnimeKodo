package fr.kitsuapirest.repository;

import fr.kitsuapirest.model.User;
import fr.kitsuapirest.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findByUserId(Integer userId);

    Optional<Watchlist> findByUser(User user);

    Optional<Watchlist> findByUser_Id(Integer userId);

}
