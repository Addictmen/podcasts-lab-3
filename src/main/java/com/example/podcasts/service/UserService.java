package com.example.podcasts.service;

import com.example.podcasts.model.Podcast;
import com.example.podcasts.model.User;
import com.example.podcasts.repository.PodcastRepository;
import com.example.podcasts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private PodcastService podcastService;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        User user = userRepository.findById(id);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        return user;
    }

    public User create(User data) {
        if (data.getName() == null || data.getName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не указано имя");
        User user = new User();
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        return userRepository.save(user);
    }

    public User update(Long id, User data) {
        User user = getById(id);
        if (data.getName() != null && data.getName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя не может быть пустым");
        if (data.getName() != null) user.setName(data.getName());
        if (data.getEmail() != null) user.setEmail(data.getEmail());
        return user;
    }

    public void delete(Long id) {
        getById(id);
        // сначала удаляем подкасты этого автора
        for (Podcast podcast : podcastRepository.findByAuthorId(id)) {
            podcastService.delete(podcast.getId());
        }
        userRepository.deleteById(id);
    }
}
