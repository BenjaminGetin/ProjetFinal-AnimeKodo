package fr.kitsuapirest.controller.api;

import fr.kitsuapirest.dto.CommentForm;
import fr.kitsuapirest.exception.UnauthorizedException;
import fr.kitsuapirest.model.Comment;
import fr.kitsuapirest.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * The CommentController class handles API endpoints related to comments on anime.
 */
@RestController
@RequestMapping("/api/animes/{animeId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Retrieves all comments for a specific anime.
     *
     * @param animeId the ID of the anime.
     * @return a ResponseEntity containing a list of Comment objects if successful, or an empty list if no comments are found.
     */
    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByAnimeId(@PathVariable Long animeId) {
        return ResponseEntity.ok(commentService.getCommentsByAnimeId(animeId));
    }

    /**
     * Retrieves a comment by its ID for a specific anime.
     *
     * @param animeId    the ID of the anime.
     * @param commentId  the ID of the comment to retrieve.
     * @param principal  the authenticated principal representing the user.
     * @return a ResponseEntity containing the Comment object if found, or ResponseEntity.notFound() if the comment is not found.
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable Long animeId, @PathVariable Long commentId, Principal principal) {
        Comment comment = commentService.getCommentByIdUserForUpdate(animeId, commentId, principal.getName());
        return ResponseEntity.ok(comment);
    }

    /**
     * Adds a new comment to a specific anime.
     *
     * @param animeId    the ID of the anime.
     * @param comment    the Comment object containing the comment details.
     * @param principal  the authenticated principal representing the user.
     * @return a ResponseEntity containing the created Comment object if successful, along with HttpStatus.CREATED.
     */
    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long animeId, @RequestBody Comment comment, @AuthenticationPrincipal Principal principal) {
        return new ResponseEntity<>(commentService.addComment(animeId, comment, principal), HttpStatus.CREATED);
    }

    /**
     * Updates an existing comment for a specific anime.
     *
     * @param animeId       the ID of the anime.
     * @param commentId     the ID of the comment to update.
     * @param commentForm   the CommentForm object containing the updated comment details.
     * @param principal     the authenticated principal representing the user.
     * @return a ResponseEntity containing the updated Comment object if successful.
     * @throws UnauthorizedException if the principal is null, indicating unauthorized access.
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long animeId, @PathVariable Long commentId, @RequestBody CommentForm commentForm, Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Unauthorized access");
        }

        return ResponseEntity.ok(commentService.updateComment(animeId, commentId, commentForm, principal));
    }

    /**
     * Deletes a comment for a specific anime.
     *
     * @param animeId    the ID of the anime.
     * @param commentId  the ID of the comment to delete.
     * @param principal  the authenticated principal representing the user.
     * @return a ResponseEntity indicating a successful deletion with ResponseEntity.noContent().
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long animeId, @PathVariable Long commentId, Principal principal) {
        commentService.deleteComment(animeId, commentId, principal);
        return ResponseEntity.noContent().build();
    }
}
