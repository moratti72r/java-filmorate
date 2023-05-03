package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final LikesDao likesDao;

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

    public ResponseEntity<?> addLike(Integer idFilm, Integer idUser) {
        return likesDao.addLike(idFilm, idUser);
    }

    public ResponseEntity<?> removeLike(Integer idFilm, Integer idUser) {
        return likesDao.removeLike(idFilm, idUser);
    }
}
