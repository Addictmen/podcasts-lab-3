package com.example.podcasts.controller;

import com.example.podcasts.model.AudioFile;
import com.example.podcasts.service.AudioFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audio-files")
public class AudioFileController {

    @Autowired
    private AudioFileService audioFileService;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (AudioFile file : audioFileService.getAll()) {
            result.add(toJson(file));
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOne(@PathVariable Long id) {
        return toJson(audioFileService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody AudioFile file) {
        return toJson(audioFileService.create(file));
    }

    @PatchMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody AudioFile file) {
        return toJson(audioFileService.update(id, file));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        audioFileService.delete(id);
    }

    // переводим объект в json для ответа
    private Map<String, Object> toJson(AudioFile file) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", file.getId());
        map.put("episodeId", file.getEpisodeId());
        map.put("path", file.getPath());
        map.put("format", file.getFormat());
        map.put("sizeBytes", file.getSizeBytes());
        map.put("main", file.getMain());
        return map;
    }
}
