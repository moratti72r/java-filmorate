package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private int idGenerator = 0;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> findAll() {
        return new ArrayList<Film>(filmStorage.getAll().values());
    }

    public Film findById(Integer id) {
        return filmStorage.getById(id);
    }

    public List<Film> findTopFilms(int limit) {
        return filmStorage.getTopFilms(limit);
    }

    public Film create(Film film) {
        idGenerator++;
        film.setId(idGenerator);
        filmStorage.getAll().put(idGenerator, film);
        log.info("Фильм успешно добавлен");
        return film;
    }


    public Film upDate(Film film) {
        if (filmStorage.contains(film.getId())) {
            filmStorage.getAll().put(film.getId(), film);
        } else {
            log.warn("Фильм с id " + film.getId() + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " отсутствует");
        }
        log.info("Фильм успешно изменен");
        return film;
    }

    public Film addLike(Integer idFilm, Integer idUser) {

        if (!userStorage.contains(idUser)) {
            log.warn("Пользватель с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + idUser + " отсутствует");
        }
        if (!filmStorage.contains(idFilm)) {
            log.warn("Фильм с id " + idFilm + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + idFilm + " отсутствует");
        }

        User user = userStorage.getById(idUser);
        Film film = findById(idFilm);

        if (user.getLikes().contains(film.getId())) {
            log.warn("Пользователь " + idUser + " уже оценивал фильм " + idFilm);
            throw new IncorrectArgumentsException("Пользователь " + idUser + " уже оценивал фильм " + idFilm);
        }

        user.getLikes().add(film.getId());
        film.setLikes(film.getLikes() + 1);
        log.info("Пользователь " + idUser + " оценил фильм " + idFilm);
        return filmStorage.getById(idFilm);

    }

    public Film removeLike(Integer idFilm, Integer idUser) {
        if (!userStorage.contains(idUser)) {
            log.warn("Пользватель с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + idUser + " отсутствует");
        }
        if (!filmStorage.contains(idFilm)) {
            log.warn("Фильм с id " + idFilm + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + idFilm + " отсутствует");
        }

        User user = userStorage.getById(idUser);
        Film film = findById(idFilm);

        if (!user.getLikes().contains(idFilm)) {
            log.warn("Пользватель " + idUser + " еще не оценивал фильм " + idFilm);
            throw new IncorrectArgumentsException("Пользватель " + idUser + " еще не оценивал фильм " + idFilm);
        }
        film.setLikes(film.getLikes() - 1);
        user.getLikes().remove(film.getId());
        log.info("Пользователь " + idUser + " удалил оценку с фильма " + idFilm);
        return film;
    }

}
