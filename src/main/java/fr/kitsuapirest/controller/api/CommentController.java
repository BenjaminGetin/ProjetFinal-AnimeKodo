package fr.kitsuapirest.controller;

import fr.kitsuapirest.entity.CommentEntity;
import fr.kitsuapirest.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/animes/{animeId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentEntity> getCommentsByAnimeId(@PathVariable Long animeId) {
        return commentService.getCommentsByAnimeId(animeId);
    }

    @PostMapping
    public CommentEntity addComment(@PathVariable Long animeId, @RequestBody CommentEntity comment) {
        return commentService.addComment(animeId, comment);
    }

    @GetMapping("/{commentId}")
    public CommentEntity getCommentById(@PathVariable Long animeId, @PathVariable Long commentId) {
        return commentService.getCommentById(animeId, commentId);
    }

    @PutMapping("/{commentId}")
    public CommentEntity updateComment(@PathVariable Long animeId, @PathVariable Long commentId,
                                       @RequestBody CommentEntity comment, Principal principal) {
        String username = principal.getName();
        return commentService.updateComment(animeId, commentId, comment, username);
    }


    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long animeId, @PathVariable Long commentId) {
        commentService.deleteComment(animeId, commentId);
    }
}
