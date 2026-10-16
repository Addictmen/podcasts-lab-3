package com.example.podcasts.repository;

import com.example.podcasts.model.Podcast;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PodcastRepository {

    private List<Podcast> podcasts = new ArrayList<>();
    private long nextId = 1;

    public List<Podcast> findAll() {
        return podcasts;
    }

    public Podcast findById(Long id) {
        for (Podcast podcast : podcasts) {
            if (podcast.getId().equals(id)) {
                return podcast;
            }
        }
        return null;
    }

    public Podcast save(Podcast podcast) {
        podcast.setId(nextId++);
        podcasts.add(podcast);
        return podcast;
    }

    public void deleteById(Long id) {
        podcasts.removeIf(x -> x.getId().equals(id));
    }

    public List<Podcast> findByAuthorId(Long authorId) {
        List<Podcast> result = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            if (podcast.getAuthorId().equals(authorId)) {
                result.add(podcast);
            }
        }
        return result;
    }
}
