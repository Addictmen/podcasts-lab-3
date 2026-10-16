package com.example.podcasts.controller;

import com.example.podcasts.model.Episode;
import com.example.podcasts.service.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/episodes")
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Episode episode : episodeService.getAll()) {
            result.add(toJson(episode));
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOne(@PathVariable Long id) {
        return toJson(episodeService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody Episode episode) {
        return toJson(episodeService.create(episode));
    }

    @PatchMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Episode episode) {
        return toJson(episodeService.update(id, episode));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        episodeService.delete(id);
    }

    // переводим объект в json для ответа
    private Map<String, Object> toJson(Episode episode) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", episode.getId());
        map.put("podcastId", episode.getPodcastId());
        map.put("title", episode.getTitle());
        map.put("durationSeconds", episode.getDurationSeconds());
        map.put("publicationState", episode.getPublicationState());
        return map;
    }
}
