package com.example.podcasts.service;

import com.example.podcasts.model.AudioFile;
import com.example.podcasts.model.Episode;
import com.example.podcasts.model.PublicationSlot;
import com.example.podcasts.model.PublicationState;
import com.example.podcasts.repository.AudioFileRepository;
import com.example.podcasts.repository.EpisodeRepository;
import com.example.podcasts.repository.PodcastRepository;
import com.example.podcasts.repository.PublicationSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private AudioFileRepository audioFileRepository;
    @Autowired
    private PublicationSlotRepository slotRepository;

    public List<Episode> getAll() {
        return episodeRepository.findAll();
    }

    public Episode getById(Long id) {
        Episode episode = episodeRepository.findById(id);
        if (episode == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Эпизод не найден");
        return episode;
    }

    public Episode create(Episode data) {
        if (data.getPodcastId() == null || data.getTitle() == null || data.getTitle().isBlank() || data.getDurationSeconds() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нужно указать podcastId, title и durationSeconds");
        if (podcastRepository.findById(data.getPodcastId()) == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Подкаст не найден");
        if (data.getDurationSeconds() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Длительность должна быть больше 0");

        Episode episode = new Episode();
        episode.setPodcastId(data.getPodcastId());
        episode.setTitle(data.getTitle());
        episode.setDurationSeconds(data.getDurationSeconds());
        episode.setPublicationState(PublicationState.DRAFT); // новый эпизод всегда черновик
        return episodeRepository.save(episode);
    }

    public Episode update(Long id, Episode data) {
        Episode episode = getById(id);
        // сначала все проверки, потом меняем
        if (data.getTitle() != null && data.getTitle().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Название не может быть пустым");
        if (data.getDurationSeconds() != null && data.getDurationSeconds() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Длительность должна быть больше 0");
        if (data.getPodcastId() != null && !data.getPodcastId().equals(episode.getPodcastId())) {
            if (podcastRepository.findById(data.getPodcastId()) == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Подкаст не найден");
            // у слотов записан старый подкаст
            if (!slotRepository.findByEpisodeId(id).isEmpty())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "У эпизода есть слоты, перенести в другой подкаст нельзя");
        }

        if (data.getTitle() != null) episode.setTitle(data.getTitle());
        if (data.getDurationSeconds() != null) episode.setDurationSeconds(data.getDurationSeconds());
        if (data.getPodcastId() != null) episode.setPodcastId(data.getPodcastId());
        if (data.getPublicationState() != null) episode.setPublicationState(data.getPublicationState());
        return episode;
    }

    public void delete(Long id) {
        getById(id);
        for (AudioFile file : audioFileRepository.findByEpisodeId(id)) {
            audioFileRepository.deleteById(file.getId());
        }
        for (PublicationSlot slot : slotRepository.findByEpisodeId(id)) {
            slotRepository.deleteById(slot.getId());
        }
        episodeRepository.deleteById(id);
    }
}
