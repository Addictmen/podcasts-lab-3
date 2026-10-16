package com.example.podcasts.controller;

import com.example.podcasts.model.Podcast;
import com.example.podcasts.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/podcasts")
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Podcast podcast : podcastService.getAll()) {
            result.add(toJson(podcast));
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOne(@PathVariable Long id) {
        return toJson(podcastService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody Podcast podcast) {
        return toJson(podcastService.create(podcast));
    }

    @PatchMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Podcast podcast) {
        return toJson(podcastService.update(id, podcast));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        podcastService.delete(id);
    }

    // переводим объект в json для ответа
    private Map<String, Object> toJson(Podcast podcast) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", podcast.getId());
        map.put("title", podcast.getTitle());
        map.put("authorId", podcast.getAuthorId());
        map.put("status", podcast.getStatus());
        return map;
    }
}
