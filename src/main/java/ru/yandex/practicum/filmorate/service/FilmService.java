package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger("FilmService.class");

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Integer idFilm, Integer idUser) {

        if (!userStorage.findAll().containsKey(idUser)) {
            log.warn("Пользватель с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + idUser + " отсутствует");
        }
        if (!filmStorage.findAll().containsKey(idFilm)) {
            log.warn("Фильм с id " + idFilm + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + idFilm + " отсутствует");
        }
        filmStorage.findAll().get(idFilm).getLike().add(idUser);
        log.info("Пользователь поставил лайк");
        return filmStorage.findAll().get(idFilm);

    }

    public Film removeLike(Integer idFilm, Integer idUser) {
        if (!userStorage.findAll().containsKey(idUser)) {
            log.warn("Пользватель с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + idUser + " отсутствует");
        }
        if (!filmStorage.findAll().containsKey(idFilm)) {
            log.warn("Фильм с id " + idFilm + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + idFilm + " отсутствует");
        }
        filmStorage.findAll().get(idFilm).getLike().remove(idUser);
        log.info("Пользователь удалил лайк");
        return filmStorage.findAll().get(idFilm);
    }

    public List<Film> getMostPopularFilms(int count) {
        if (!filmStorage.findAll().isEmpty()) {
            List<Film> sortingList = filmStorage.findAll().values().stream().sorted((f1, f2) -> {
                        return -1 * (f1.getLike().size() - f2.getLike().size());
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
