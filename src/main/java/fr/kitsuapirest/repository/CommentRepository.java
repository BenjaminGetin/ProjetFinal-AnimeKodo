package fr.kitsuapirest.repository;

import fr.kitsuapirest.model.Comment;
import fr.kitsuapirest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAnimeId(Long animeId);

    Optional<Comment> findById(Long id);

    List<Comment> findByUser(User user);

}
