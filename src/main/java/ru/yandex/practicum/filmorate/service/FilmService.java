package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    @Getter
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLike(Integer idFilm, Integer idUser) {

        if (!userStorage.contains(idUser)) {
            log.warn("Пользватель с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + idUser + " отсутствует");
        }
        if (!filmStorage.contains(idFilm)) {
            log.warn("Фильм с id " + idFilm + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + idFilm + " отсутствует");
        }
        if (userStorage.getById(idUser).getLikes().contains(idFilm)) {
            log.warn("Пользователь " + idUser + " уже оценивал фильм " + idFilm);
            throw new IncorrectArgumentsException("Пользователь " + idUser + " уже оценивал фильм " + idFilm);
        }
        filmStorage.getById(idFilm).setLikes(filmStorage.getById(idFilm).getLikes() + 1);
        userStorage.getById(idUser).getLikes().add(filmStorage.getById(idFilm).getId());
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
        if (!userStorage.getById(idUser).getLikes().contains(idFilm)) {
            log.warn("Пользватель " + idUser + " еще не оценивал фильм " + idFilm);
            throw new IncorrectArgumentsException("Пользватель " + idUser + " еще не оценивал фильм " + idFilm);
        }
        filmStorage.getById(idFilm).setLikes(filmStorage.getById(idFilm).getLikes() - 1);
        userStorage.getById(idUser).getLikes().remove(filmStorage.getById(idFilm).getId());
        log.info("Пользователь " + idUser + " удалил оценку с фильма " + idFilm);
        return filmStorage.findAll().get(idFilm);
    }

    public List<Film> getMostPopularFilms(int count) {
        if (!filmStorage.findAll().isEmpty()) {
            List<Film> sortingList = filmStorage.findAll().values().stream().sorted((f1, f2) -> {
                        return -1 * (f1.getLikes() - f2.getLikes());
                    })
                    .collect(Collectors.toList());

            if (count == 0) {
                log.info("Получены 10 наиболее популярных фильмов");
                return sortingList.stream().limit(10).collect(Collectors.toList());
            } else {
                log.info("Получены " + count + " наиболее популярных фильмов");
                return sortingList.stream().limit(count).collect(Collectors.toList());
            }
        } else {
            log.warn("Список фильмов пустой");
            throw new FilmNotFoundException("Список фильмов пустой");
        }
    }
}
