package com.example.podcasts.service;

import com.example.podcasts.model.AudioFile;
import com.example.podcasts.model.Episode;
import com.example.podcasts.model.PublicationState;
import com.example.podcasts.repository.AudioFileRepository;
import com.example.podcasts.repository.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AudioFileService {

    @Autowired
    private AudioFileRepository audioFileRepository;
    @Autowired
    private EpisodeRepository episodeRepository;

    public List<AudioFile> getAll() {
        return audioFileRepository.findAll();
    }

    public AudioFile getById(Long id) {
        AudioFile file = audioFileRepository.findById(id);
        if (file == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Аудиофайл не найден");
        return file;
    }

    public AudioFile create(AudioFile data) {
        if (data.getEpisodeId() == null || data.getPath() == null || data.getPath().isBlank() || data.getFormat() == null || data.getSizeBytes() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нужно указать episodeId, path, format и sizeBytes");
        if (episodeRepository.findById(data.getEpisodeId()) == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Эпизод не найден");
        if (data.getSizeBytes() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Размер должен быть больше 0");

        AudioFile file = new AudioFile();
        file.setEpisodeId(data.getEpisodeId());
        file.setPath(data.getPath());
        file.setFormat(data.getFormat());
        file.setSizeBytes(data.getSizeBytes());
        // первый файл эпизода становится основным
        boolean main = audioFileRepository.findByEpisodeId(data.getEpisodeId()).isEmpty();
        if (data.getMain() != null) main = data.getMain();
        file.setMain(false);
        audioFileRepository.save(file);
        if (main) makeMain(file);
        return file;
    }

    public AudioFile update(Long id, AudioFile data) {
        AudioFile file = getById(id);
        if (data.getPath() != null && data.getPath().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Путь не может быть пустым");
        if (data.getSizeBytes() != null && data.getSizeBytes() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Размер должен быть больше 0");
        if (data.getMain() != null && !data.getMain() && file.getMain())
            checkCanRemoveMain(file.getEpisodeId());

        if (data.getPath() != null) file.setPath(data.getPath());
        if (data.getFormat() != null) file.setFormat(data.getFormat());
        if (data.getSizeBytes() != null) file.setSizeBytes(data.getSizeBytes());
        if (data.getMain() != null) {
            if (data.getMain()) {
                makeMain(file);
            } else {
                file.setMain(false);
            }
        }
        return file;
    }

    public void delete(Long id) {
        AudioFile file = getById(id);
        if (file.getMain()) {
            // ищем другой файл этого эпизода, чтобы сделать его основным
            AudioFile other = null;
            for (AudioFile f : audioFileRepository.findByEpisodeId(file.getEpisodeId())) {
                if (!f.getId().equals(id)) other = f;
            }
            if (other == null) {
                checkCanRemoveMain(file.getEpisodeId());
            } else {
                other.setMain(true);
            }
        }
        audioFileRepository.deleteById(id);
    }

    // у запланированного или опубликованного эпизода должен остаться основной файл
    private void checkCanRemoveMain(Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId);
        if (episode.getPublicationState() != PublicationState.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Эпизод запланирован или опубликован, без основного файла нельзя");
    }

    // основной файл у эпизода может быть только один
    private void makeMain(AudioFile file) {
        for (AudioFile other : audioFileRepository.findByEpisodeId(file.getEpisodeId())) {
            other.setMain(false);
        }
        file.setMain(true);
    }
}
