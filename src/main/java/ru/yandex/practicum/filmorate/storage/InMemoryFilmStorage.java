package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int idGenerator = 0;

    private final Map<Integer, Film> films = new HashMap<>();


    @Override
    public List<Film> getAll() {
        log.info("Список фильмов получен");
        return new ArrayList<Film>(films.values());
    }

    @Override
    public Film create(Film film) {
        idGenerator++;
        film.setId(idGenerator);
        films.put(idGenerator, film);
        log.info("Фильм с id=" + film.getId() + " успешно добавлен");
        return film;
    }

    @Override
    public Film upDate(Film film) {
        if (contains(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.warn("Фильм с id " + film.getId() + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " отсутствует");
        }
        log.info("Фильм с id=" + film.getId() + " успешно изменен");
        return film;
    }

    @Override
    public List<Film> getTopFilms(int limit) {
        log.info("Получены " + limit + " наиболее популярных фильмов");
        return films.values().stream().sorted((f1, f2) -> {
                    return -1 * (f1.getLikes() - f2.getLikes());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }


    @Override
    public boolean contains(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Film getById(Integer id) {
        if (films.containsKey(id)) {
            log.info("Фильм с id " + id + " получен");
            return films.get(id);
        } else {
            log.warn("Фильм с id " + id + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + id + " отсутствует");
        }
    }

    @Override
    public List<Genres> getAllGenres() {
        return new ArrayList<>(InMemoryGenreStorage.getGenres().values());
    }

    @Override
    public Genres getGenreById(Integer id) {
        return InMemoryGenreStorage.getGenres().get(id);
    }

    @Override
    public List<MPA> getAllMPA() {
        return new ArrayList<>(InMemoryMPAStorage.getMpa().values());
    }

    @Override
    public MPA getMpaById(Integer id) {
        return InMemoryMPAStorage.getMpa().get(id);
    }

}
