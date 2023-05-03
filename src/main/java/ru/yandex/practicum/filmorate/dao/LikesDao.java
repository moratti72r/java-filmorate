package ru.yandex.practicum.filmorate.dao;

import org.springframework.http.ResponseEntity;

public interface LikesDao {
    ResponseEntity<?> addLike(int idFilm, int idUser);

    ResponseEntity<?> removeLike(int idFilm, int idUser);
}
