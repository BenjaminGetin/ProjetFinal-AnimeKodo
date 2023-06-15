package fr.kitsuapirest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Minimum rating is 1")
    @Max(value = 5, message = "Maximum rating is 5")
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;

    @Column(unique = true)
    private String userAnimeKey;

    public Rating() {

    }

    public Rating(Integer rating, User user, Anime anime, String userAnimeKey) {
        this.rating = rating;
        this.user = user;
        this.anime = anime;
        this.userAnimeKey = userAnimeKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public String getUserAnimeKey() {
        return userAnimeKey;
    }

    public void setUserAnimeKey(String userAnimeKey) {
        this.userAnimeKey = userAnimeKey;
    }
}


