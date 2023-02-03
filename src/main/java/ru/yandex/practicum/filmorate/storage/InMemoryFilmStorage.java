package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private int idGenerator = 0;

    @Override
    public Map<Integer, Film> findAll() {
        log.info("Список фильмов получен");
        return films;
    }

    @Override
    public List<Film> getSortFilms() {
        if (!films.isEmpty()) {
            return films.values().stream().sorted((f1, f2) -> {
                        return -1 * (f1.getLikes() - f2.getLikes());
                    })
                    .collect(Collectors.toList());
        } else {
            log.warn("Список для сортировки не должен быть пустым");
            throw new FilmNotFoundException("Список для сортировки не должен быть пустым");
        }
    }


    @Override
    public boolean contains(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Film getById(Integer id) {
        if (films.containsKey(id)) {
            log.info("Фильм с id " + id + " получен");
            return findAll().get(id);
        } else {
            log.warn("Фильм с id " + id + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + id + " отсутствует");
        }
    }

    @Override
    public Film create(Film film) {
        idGenerator++;
        film.setId(idGenerator);
        films.put(idGenerator, film);
        log.info("Фильм успешно добавлен");
        return film;
    }

    @Override
    public Film upDate(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.warn("Фильм с id " + film.getId() + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " отсутствует");
        }
        log.info("Фильм успешно изменен");
        return film;
    }
}
