package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/films")
@RestController
@Validated
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger("FilmController.class");

    private Map<Integer, Film> films = new HashMap<>();

    private int idGenerator = 0;

    @GetMapping()
    public ResponseEntity<?> findAll() {
        log.info("Получен GET запрос.");
        return new ResponseEntity<>(films.values(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid Film film) {
        idGenerator++;
        film.setId(idGenerator);
        films.put(idGenerator, film);
        log.info("Фильм добавлен");
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<?> upDate(@RequestBody @Valid Film film) {
        if (!films.isEmpty()) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм с id " + film.getId() + " изменен");
            } else {
                log.warn("Фильм с id " + film.getId() + " отсутствует");
                return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
            }
        } else {
            log.warn("Фильмы отсутствуют");
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(film, HttpStatus.OK);
    }
}

