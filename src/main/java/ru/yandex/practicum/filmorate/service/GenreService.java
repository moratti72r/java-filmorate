package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genres> findAll() {
        log.info("Список жанров получен");
        return genreStorage.getAll();
    }

    public Genres findById(int id) {
        if (genreStorage.contains(id)) {
            log.info("Жанр с id={} получен", id);
            return genreStorage.getById(id);
        } else {
            throw new EntityNotFoundException(GenreService.class);
        }
    }
}
