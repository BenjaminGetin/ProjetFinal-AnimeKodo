package fr.kitsuapirest.model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "anime")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String image;

    @Column(columnDefinition = "LONGTEXT")
    private String synopsis;


    private LocalDate startDate;


    private LocalDate endDate;


    private Integer episodeCount;


    private Integer episodeLength;

    private String subtype;

    private String status;


    private String ageRatingGuide;

    public Anime() {

    }

    public Anime(String title, String image, String synopsis, LocalDate startDate, LocalDate endDate,
                 Integer episodeCount, Integer episodeLength, String subtype, String status,
                 String ageRatingGuide) {
        this.title = title;
        this.image = image;
        this.synopsis = synopsis;
        this.startDate = startDate;
        this.endDate = endDate;
        this.episodeCount = episodeCount;
        this.episodeLength = episodeLength;
        this.subtype = subtype;
        this.status = status;
        this.ageRatingGuide = ageRatingGuide;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Integer getEpisodeLength() {
        return episodeLength;
    }

    public void setEpisodeLength(Integer episodeLength) {
        this.episodeLength = episodeLength;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgeRatingGuide() {
        return ageRatingGuide;
    }

    public void setAgeRatingGuide(String ageRatingGuide) {
        this.ageRatingGuide = ageRatingGuide;
    }
}
