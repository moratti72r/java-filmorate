package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

@RequestMapping("/films")
@RestController
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;

    @GetMapping()
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping()
    public Film create(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    @PutMapping()
    public Film upDate(@RequestBody @Valid Film film) {
        return filmService.upDate(film);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        return filmService.findById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") @Min(value = 0) Integer count) {
        return filmService.findTopFilms(count);
    }
}

