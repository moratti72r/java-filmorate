package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikesDao;

@Component
@AllArgsConstructor
public class LikesDaoImpl implements LikesDao {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public int addLike(int idFilm, int idUser) {

        int result = jdbcTemplate.update("INSERT INTO likes (id_user,id_film) VALUES (?,?)", idUser, idFilm);
        if (result != 0) upDateLikesFromFilm(idFilm);
        return result;
    }

    @Override
    public int removeLike(int idFilm, int idUser) {

        int result = jdbcTemplate.update("DELETE FROM likes WHERE id_user = ? AND id_film = ?", idUser, idFilm);
        if (result != 0) upDateLikesFromFilm(idFilm);
        return result;
    }

    private void upDateLikesFromFilm(int idFilm) {
        String sqlQuery = "UPDATE films SET likes = (SELECT COUNT (id_user) FROM likes WHERE id_film = ?)  where id = ?";
        jdbcTemplate.update(sqlQuery, idFilm, idFilm);
    }
}
