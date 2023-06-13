package fr.kitsuapirest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de création ne peut pas être nulle")
    private LocalDateTime createdAt;

    @NotEmpty(message = "Le contenu du commentaire ne peut pas être vide")
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull(message = "L'ID de l'utilisateur ne peut pas être nul")
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @NotNull(message = "L'ID de l'anime ne peut pas être nul")
    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;

    public Comment() {

    }

    public Comment(String content, User user, Anime anime) {
        this.content = content;
        this.user = user;
        this.anime = anime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }
}