package com.example.podcasts.model;

public class AudioFile {

    private Long id;
    private Long episodeId;
    private String path;
    private AudioFormat format;
    private Long sizeBytes;
    private Boolean main;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(Long episodeId) {
        this.episodeId = episodeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AudioFormat getFormat() {
        return format;
    }

    public void setFormat(AudioFormat format) {
        this.format = format;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }
}
