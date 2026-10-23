
package com.example.podcasts.repository;

import com.example.podcasts.model.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {

    List<AudioFile> findByEpisodeId(Long episodeId);

}
