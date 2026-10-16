package com.example.podcasts.repository;

import com.example.podcasts.model.AudioFile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AudioFileRepository {

    private List<AudioFile> files = new ArrayList<>();
    private long nextId = 1;

    public List<AudioFile> findAll() {
        return files;
    }

    public AudioFile findById(Long id) {
        for (AudioFile file : files) {
            if (file.getId().equals(id)) {
                return file;
            }
        }
        return null;
    }

    public AudioFile save(AudioFile file) {
        file.setId(nextId++);
        files.add(file);
        return file;
    }

    public void deleteById(Long id) {
        files.removeIf(x -> x.getId().equals(id));
    }

    public List<AudioFile> findByEpisodeId(Long episodeId) {
        List<AudioFile> result = new ArrayList<>();
        for (AudioFile file : files) {
            if (file.getEpisodeId().equals(episodeId)) {
                result.add(file);
            }
        }
        return result;
    }
}
