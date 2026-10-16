package com.example.podcasts.repository;

import com.example.podcasts.model.Episode;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EpisodeRepository {

    private List<Episode> episodes = new ArrayList<>();
    private long nextId = 1;

    public List<Episode> findAll() {
        return episodes;
    }

    public Episode findById(Long id) {
        for (Episode episode : episodes) {
            if (episode.getId().equals(id)) {
                return episode;
            }
        }
        return null;
    }

    public Episode save(Episode episode) {
        episode.setId(nextId++);
        episodes.add(episode);
        return episode;
    }

    public void deleteById(Long id) {
        episodes.removeIf(x -> x.getId().equals(id));
    }

    public List<Episode> findByPodcastId(Long podcastId) {
        List<Episode> result = new ArrayList<>();
        for (Episode episode : episodes) {
            if (episode.getPodcastId().equals(podcastId)) {
                result.add(episode);
            }
        }
        return result;
    }
}
