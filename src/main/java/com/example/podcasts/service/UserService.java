
package com.example.podcasts.service;

import com.example.podcasts.model.Podcast;
import com.example.podcasts.model.User;
import com.example.podcasts.repository.PodcastRepository;
import com.example.podcasts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Пользователь не найден"
                ));
    }

    @Transactional
    public User create(User data) {
        if (data.getName() == null || data.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Не указано имя"
            );
        }

        if (data.getEmail() == null || data.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Не указан email"
            );
        }

        User user = new User();
        user.setName(data.getName());
        user.setEmail(data.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, User data) {
        User user = getById(id);

        if (data.getName() != null) {
            if (data.getName().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Имя не может быть пустым"
                );
            }
            user.setName(data.getName());
        }

        if (data.getEmail() != null) {
            if (data.getEmail().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Email не может быть пустым"
                );
            }
            user.setEmail(data.getEmail());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        getById(id);

        // Сначала удаляем подкасты пользователя
        for (Podcast podcast : podcastRepository.findByAuthorId(id)) {
            podcastService.delete(podcast.getId());
        }

        // Затем удаляем самого пользователя
        userRepository.deleteById(id);
    }
}
