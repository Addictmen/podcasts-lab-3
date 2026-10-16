package com.example.podcasts.model;

public class Podcast {

    private Long id;
    private String title;
    private Long authorId;
    private PodcastStatus status;

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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public PodcastStatus getStatus() {
        return status;
    }

    public void setStatus(PodcastStatus status) {
        this.status = status;
    }
}
