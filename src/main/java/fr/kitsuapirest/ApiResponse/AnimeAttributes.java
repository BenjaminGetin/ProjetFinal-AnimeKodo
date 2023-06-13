package fr.kitsuapirest.ApiResponse;

import java.time.LocalDate;

public class AnimeAttributes {

    private Titles titles;
    private String synopsis;
    private String imageUrl;
    private String status;
    private Integer episodeCount;
    private Integer episodeLength;
    private Double averageRating;
    private LocalDate startDate;
    private LocalDate endDate;
    private ImageObject coverImage;
    private ImageObject posterImage;
    private String subtype;
    private String ageRatingGuide;


    public Titles getTitles() {
        return titles;
    }

    public void setTitles(Titles titles) {
        this.titles = titles;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
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

    public ImageObject getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(ImageObject coverImage) {
        this.coverImage = coverImage;
    }

    public ImageObject getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(ImageObject posterImage) {
        this.posterImage = posterImage;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getAgeRatingGuide() {
        return ageRatingGuide;
    }

    public void setAgeRatingGuide(String ageRatingGuide) {
        this.ageRatingGuide = ageRatingGuide;
    }
}