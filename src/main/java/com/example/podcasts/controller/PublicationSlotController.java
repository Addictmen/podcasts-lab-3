package com.example.podcasts.controller;

import com.example.podcasts.model.PublicationSlot;
import com.example.podcasts.service.PublicationSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publication-slots")
public class PublicationSlotController {

    @Autowired
    private PublicationSlotService publicationSlotService;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (PublicationSlot slot : publicationSlotService.getAll()) {
            result.add(toJson(slot));
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOne(@PathVariable Long id) {
        return toJson(publicationSlotService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody PublicationSlot slot) {
        return toJson(publicationSlotService.create(slot));
    }

    @PatchMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody PublicationSlot slot) {
        return toJson(publicationSlotService.update(id, slot));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        publicationSlotService.delete(id);
    }

    // переводим объект в json для ответа
    private Map<String, Object> toJson(PublicationSlot slot) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", slot.getId());
        map.put("podcastId", slot.getPodcastId());
        map.put("episodeId", slot.getEpisodeId());
        map.put("dateTime", slot.getDateTime());
        map.put("status", slot.getStatus());
        return map;
    }
}
