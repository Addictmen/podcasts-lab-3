package com.example.podcasts.service;

import com.example.podcasts.model.AudioFile;
import com.example.podcasts.model.Episode;
import com.example.podcasts.model.PublicationSlot;
import com.example.podcasts.model.PublicationState;
import com.example.podcasts.model.SlotStatus;
import com.example.podcasts.repository.AudioFileRepository;
import com.example.podcasts.repository.EpisodeRepository;
import com.example.podcasts.repository.PublicationSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublicationSlotService {

    @Autowired
    private PublicationSlotRepository slotRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private AudioFileRepository audioFileRepository;

    public List<PublicationSlot> getAll() {
        return slotRepository.findAll();
    }

    public PublicationSlot getById(Long id) {
        PublicationSlot slot = slotRepository.findById(id);
        if (slot == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Слот не найден");
        return slot;
    }

    public PublicationSlot create(PublicationSlot data) {
        if (data.getPodcastId() == null || data.getEpisodeId() == null || data.getDateTime() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нужно указать podcastId, episodeId и dateTime");
        Episode episode = episodeRepository.findById(data.getEpisodeId());
        if (episode == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Эпизод не найден");
        if (!episode.getPodcastId().equals(data.getPodcastId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Эпизод не из этого подкаста");
        // сравниваем с текущим временем сервера
        if (data.getDateTime().isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата публикации уже прошла");
        if (episode.getPublicationState() != PublicationState.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Эпизод уже запланирован или опубликован");
        if (!hasMainFile(episode.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У эпизода нет основного аудиофайла");

        PublicationSlot slot = new PublicationSlot();
        slot.setPodcastId(data.getPodcastId());
        slot.setEpisodeId(data.getEpisodeId());
        slot.setDateTime(data.getDateTime());
        slot.setStatus(SlotStatus.PLANNED);
        episode.setPublicationState(PublicationState.SCHEDULED);
        return slotRepository.save(slot);
    }

    public PublicationSlot update(Long id, PublicationSlot data) {
        PublicationSlot slot = getById(id);
        if (data.getDateTime() != null) {
            if (data.getDateTime().isBefore(LocalDateTime.now()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата публикации уже прошла");
            slot.setDateTime(data.getDateTime());
        }
        if (data.getStatus() != null) {
            slot.setStatus(data.getStatus());
            // меняем состояние эпизода вместе со слотом
            Episode episode = episodeRepository.findById(slot.getEpisodeId());
            if (data.getStatus() == SlotStatus.DONE) episode.setPublicationState(PublicationState.PUBLISHED);
            if (data.getStatus() == SlotStatus.CANCELLED) episode.setPublicationState(PublicationState.DRAFT);
        }
        return slot;
    }

    public void delete(Long id) {
        PublicationSlot slot = getById(id);
        // если слот был запланирован, эпизод снова становится черновиком
        if (slot.getStatus() == SlotStatus.PLANNED) {
            Episode episode = episodeRepository.findById(slot.getEpisodeId());
            episode.setPublicationState(PublicationState.DRAFT);
        }
        slotRepository.deleteById(id);
    }

    private boolean hasMainFile(Long episodeId) {
        for (AudioFile file : audioFileRepository.findByEpisodeId(episodeId)) {
            if (file.getMain()) return true;
        }
        return false;
    }
}
