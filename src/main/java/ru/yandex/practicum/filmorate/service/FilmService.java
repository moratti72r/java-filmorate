package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film findById(Integer id) {
        return filmStorage.getById(id);
    }

    public List<Film> findTopFilms(int limit) {
        return filmStorage.getTopFilms(limit);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }


    public Film upDate(Film film) {
        return filmStorage.upDate(film);
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

        if (user.getLikesOnMovies().contains(film.getId())) {
            log.warn("Пользователь " + idUser + " уже оценивал фильм " + idFilm);
            throw new IncorrectArgumentsException("Пользователь " + idUser + " уже оценивал фильм " + idFilm);
        }

        user.getLikesOnMovies().add(film.getId());
        film.setLikes(film.getLikes() + 1);

        userStorage.upDate(user);
        log.info("Пользователь " + idUser + " оценил фильм " + idFilm);
        return filmStorage.upDate(film);

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

        if (!user.getLikesOnMovies().contains(idFilm)) {
            log.warn("Пользватель " + idUser + " еще не оценивал фильм " + idFilm);
            throw new IncorrectArgumentsException("Пользватель " + idUser + " еще не оценивал фильм " + idFilm);
        }
        film.setLikes(film.getLikes() - 1);
        user.getLikesOnMovies().remove(film.getId());

        userStorage.upDate(user);
        log.info("Пользователь " + idUser + " удалил оценку с фильма " + idFilm);
        return filmStorage.upDate(film);
    }

    public List<Genres> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genres getGenreById(Integer id) {
        return filmStorage.getGenreById(id);
    }

    public List<MPA> getAllMpa() {
        return filmStorage.getAllMPA();
    }

    public MPA getMpaById(Integer id) {
        return filmStorage.getMpaById(id);
    }
}
