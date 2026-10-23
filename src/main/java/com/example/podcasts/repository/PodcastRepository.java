
package com.example.podcasts.repository;

import com.example.podcasts.model.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PodcastRepository extends JpaRepository<Podcast, Long> {

    List<Podcast> findByAuthorId(Long authorId);

}
