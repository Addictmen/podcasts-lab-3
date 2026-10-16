package com.example.podcasts.repository;

import com.example.podcasts.model.PublicationSlot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PublicationSlotRepository {

    private List<PublicationSlot> slots = new ArrayList<>();
    private long nextId = 1;

    public List<PublicationSlot> findAll() {
        return slots;
    }

    public PublicationSlot findById(Long id) {
        for (PublicationSlot slot : slots) {
            if (slot.getId().equals(id)) {
                return slot;
            }
        }
        return null;
    }

    public PublicationSlot save(PublicationSlot slot) {
        slot.setId(nextId++);
        slots.add(slot);
        return slot;
    }

    public void deleteById(Long id) {
        slots.removeIf(x -> x.getId().equals(id));
    }

    public List<PublicationSlot> findByEpisodeId(Long episodeId) {
        List<PublicationSlot> result = new ArrayList<>();
        for (PublicationSlot slot : slots) {
            if (slot.getEpisodeId().equals(episodeId)) {
                result.add(slot);
            }
        }
        return result;
    }
}
