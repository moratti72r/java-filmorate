package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Component
@AllArgsConstructor
public class LikesDaoImpl implements LikesDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;


    @Override
    public ResponseEntity<?> addLike(int idFilm, int idUser) {
        if (contains(idFilm, idUser)) {
            throw new IncorrectArgumentsException(LikesDaoImpl.class);
        }
        if (!filmStorage.contains(idFilm) || !userStorage.contains(idUser)) {
            throw new EntityNotFoundException(LikesDaoImpl.class);
        }
        jdbcTemplate.update("INSERT INTO likes (id_user,id_film) VALUES (?,?)", idUser, idFilm);
        jdbcTemplate.update("UPDATE films SET likes = likes + 1 WHERE id = ?", idFilm);
        log.info("Пользователь c id={} оценил фильм c id={}", idUser, idFilm);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> removeLike(int idFilm, int idUser) {
        if (!userStorage.contains(idUser) || !filmStorage.contains(idFilm) || !contains(idFilm, idUser)) {
            throw new EntityNotFoundException(LikesDaoImpl.class);
        }
        jdbcTemplate.update("DELETE FROM likes WHERE id_user = ? AND id_film = ?", idUser, idFilm);
        jdbcTemplate.update("UPDATE films SET likes = likes - 1 WHERE id = ?", idFilm);
        log.info("Пользователь c id={} удалил оценку с фильма c id={}", idUser, idFilm);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    private boolean contains(int idFilm, int idUser) {
        SqlRowSet likesRow = jdbcTemplate.queryForRowSet("SELECT 1 FROM likes " +
                        "WHERE id_user = ? AND id_film = ?",
                idUser, idFilm);
        return likesRow.next();
    }
}
