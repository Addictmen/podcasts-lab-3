
package com.example.podcasts.repository;

import com.example.podcasts.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    List<Episode> findByPodcastId(Long podcastId);

}
