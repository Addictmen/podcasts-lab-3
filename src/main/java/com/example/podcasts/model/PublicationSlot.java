package com.example.podcasts.model;

import java.time.LocalDateTime;

public class PublicationSlot {

    private Long id;
    private Long podcastId;
    private Long episodeId;
    private LocalDateTime dateTime;
    private SlotStatus status;

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

    public Long getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(Long episodeId) {
        this.episodeId = episodeId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }
}
