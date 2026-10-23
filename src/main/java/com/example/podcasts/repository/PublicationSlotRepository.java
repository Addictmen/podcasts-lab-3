
package com.example.podcasts.repository;

import com.example.podcasts.model.PublicationSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationSlotRepository
        extends JpaRepository<PublicationSlot, Long> {

    List<PublicationSlot> findByEpisodeId(Long episodeId);

}
