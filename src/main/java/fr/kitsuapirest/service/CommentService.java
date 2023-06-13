package fr.kitsuapirest.service;

import fr.kitsuapirest.dto.CommentForm;
import fr.kitsuapirest.exception.NotFoundException;
import fr.kitsuapirest.exception.UnauthorizedException;
import fr.kitsuapirest.model.Anime;
import fr.kitsuapirest.model.Comment;
import fr.kitsuapirest.model.User;
import fr.kitsuapirest.repository.AnimeRepository;
import fr.kitsuapirest.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The CommentService class handles operations related to comments.
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AnimeService animeService;
    private final AnimeRepository animeRepository;

    @Autowired
    public CommentService(
            CommentRepository commentRepository,
            UserService userService,
            AnimeService animeService,
            AnimeRepository animeRepository
    ) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.animeService = animeService;
        this.animeRepository = animeRepository;
    }

    /**
     * Retrieves comments by anime ID.
     *
     * @param animeId the ID of the anime.
     * @return a list of comments for the anime.
     */
    public List<Comment> getCommentsByAnimeId(Long animeId) {
        return commentRepository.findByAnimeId(animeId);
    }

    /**
     * Adds a new comment for an anime.
     *
     * @param animeId   the ID of the anime.
     * @param comment   the comment to add.
     * @param principal the principal object containing user information.
     * @return the created comment.
     */
    public Comment addComment(Long animeId, Comment comment, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        Anime anime = animeService.getAnimeById(animeId);

        comment.setUser(user);
        comment.setAnime(anime);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    /**
     * Retrieves a comment by anime ID and comment ID.
     *
     * @param animeId   the ID of the anime.
     * @param commentId the ID of the comment.
     * @return the comment if found.
     * @throws NotFoundException      if the comment is not found.
     * @throws UnauthorizedException  if the user is not authorized to access the comment.
     */
    public Comment getCommentById(Long animeId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        // Check if the comment belongs to the specified anime
        if (!comment.getAnime().equals(animeId)) {
            throw new UnauthorizedException("Unauthorized access to comment");
        }

        return comment;
    }

    /**
     * Updates a comment by anime ID, comment ID, and form data.
     *
     * @param animeId     the ID of the anime.
     * @param commentId   the ID of the comment.
     * @param commentForm the updated comment form.
     * @param principal   the principal object containing user information.
     * @return the updated comment.
     * @throws UnauthorizedException if the user is not authorized to update the comment.
     * @throws RuntimeException     if failed to update the comment.
     */
    public Comment updateComment(Long animeId, Long commentId, CommentForm commentForm, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Comment comment = getCommentByIdUserForUpdate(animeId, commentId, user.getUsername());

        try {
            comment.setContent(commentForm.getContent());
            return commentRepository.save(comment);
        } catch (Exception e) {
            // Handle the exception here (e.g., display or log)
            e.printStackTrace();
            throw new RuntimeException("Failed to update comment");
        }
    }

    /**
     * Deletes a comment by anime ID and comment ID.
     *
     * @param animeId   the ID of the anime.
     * @param commentId the ID of the comment.
     * @param principal the principal object containing user information.
     * @throws UnauthorizedException if the user is not authorized to delete the comment.
     */
    public void deleteComment(Long animeId, Long commentId, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Comment comment = getCommentByIdUserForDelete(animeId, commentId, user.getUsername(), user.getRole().toString());

        commentRepository.delete(comment);
    }

    /**
     * Retrieves a comment by anime ID, comment ID, and username for deletion.
     *
     * @param animeId   the ID of the anime.
     * @param commentId the ID of the comment.
     * @param username  the username of the user.
     * @param role      the role of the user.
     * @return the comment if found.
     * @throws NotFoundException      if the comment is not found.
     * @throws UnauthorizedException  if the user is not authorized to access the comment.
     */
    public Comment getCommentByIdUserForDelete(Long animeId, Long commentId, String username, String role) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        // Check if the comment belongs to the specified anime and user, or if the user is an administrator
        if (!comment.getAnime().getId().equals(animeId)
                || (!comment.getUser().getUsername().equals(username) && !role.equals("ADMIN"))) {
            throw new UnauthorizedException("Unauthorized access to comment");
        }

        return comment;
    }

    /**
     * Retrieves a comment by anime ID, comment ID, and username for update.
     *
     * @param animeId   the ID of the anime.
     * @param commentId the ID of the comment.
     * @param username  the username of the user.
     * @return the comment if found.
     * @throws NotFoundException      if the comment is not found.
     * @throws UnauthorizedException  if the user is not authorized to access the comment.
     */
    public Comment getCommentByIdUserForUpdate(Long animeId, Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        // Check if the comment belongs to the specified anime and user
        if (!comment.getAnime().getId().equals(animeId) || !comment.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Unauthorized access to comment");
        }

        return comment;
    }

    /**
     * Retrieves comments by user ID.
     *
     * @param username the username of the user.
     * @return a list of comments by the user.
     */
    public List<Comment> getCommentsByUserId(String username) {
        User user = userService.getUserByUsername(username);
        List<Comment> comments = commentRepository.findByUser(user);
        return comments;
    }
}
