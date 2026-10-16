package com.example.podcasts.model;

public class Episode {

    private Long id;
    private Long podcastId;
    private String title;
    private Integer durationSeconds;
    private PublicationState publicationState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(Long podcastId) {
        this.podcastId = podcastId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public PublicationState getPublicationState() {
        return publicationState;
    }

    public void setPublicationState(PublicationState publicationState) {
        this.publicationState = publicationState;
    }
}
