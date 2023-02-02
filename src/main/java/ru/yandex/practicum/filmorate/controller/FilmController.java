package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/films")
@RestController
@Validated
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger("FilmController.class");

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, InMemoryFilmStorage filmStorage) {

        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping()
    public List<Film> findAll() {
        log.info("Получен GET запрос.");
        return new ArrayList<Film>(filmStorage.findAll().values());
    }

    @PostMapping()
    public Film create(@RequestBody @Valid Film film) {
        return filmStorage.create(film);
    }

    @PutMapping()
    public Film upDate(@RequestBody @Valid Film film) {
        return filmStorage.upDate(film);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        return filmStorage.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        if (count == null) {
            return filmService.getMostPopularFilms(0);
        } else {
            return filmService.getMostPopularFilms(count);
        }
    }
}

