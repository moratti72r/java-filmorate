package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final LikesDao likesDao;

    public List<Film> findAll() {
        log.info("Список фильмов получен");
        return filmStorage.getAll();
    }

    public Film findById(int id) {
        if (filmStorage.contains(id)) {
            log.info("Фильм с id={} получен", id);
            return filmStorage.getById(id);
        } else {
            throw new EntityNotFoundException(FilmService.class);
        }
    }

    public List<Film> findTopFilms(int limit) {
        log.info("Получены {} наиболее популярных фильмов", limit);
        return filmStorage.getTopFilms(limit);
    }

    public Film create(Film film) {
        log.info("Фильм с id={} успешно добавлен", film.getId());
        return filmStorage.create(film);
    }


    public Film upDate(Film film) {
        int result = filmStorage.upDate(film);
        if (result == 0) {
            throw new EntityNotFoundException(FilmService.class);
        } else {
            log.info("Фильм с id={} успешно изменен", film.getId());
            return film;
        }
    }

    public void addLike(int idFilm, int idUser) {
        int result = likesDao.addLike(idFilm, idUser);
        if (result == 0) {
            throw new IncorrectArgumentsException(FilmService.class);
        }
        if (!filmStorage.contains(idFilm) || !userStorage.contains(idUser)) {
            throw new EntityNotFoundException(FilmService.class);
        }
        log.info("Пользователь c id={} оценил фильм c id={}", idUser, idFilm);
    }

    public void removeLike(int idFilm, int idUser) {
        int result = likesDao.removeLike(idFilm, idUser);
        if (!userStorage.contains(idUser) || !filmStorage.contains(idFilm) || result == 0) {
            throw new EntityNotFoundException(FilmService.class);
        }
        log.info("Пользователь c id={} удалил оценку с фильма c id={}", idUser, idFilm);
    }
}
