package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genres> getAll() {
        String sq = "SELECT * FROM genres";
        log.info("Список жанров получен");
        return jdbcTemplate.query(sq, genreRowMapper());
    }

    @Override
    public Genres getById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from genres where id = ?", id);
        if (filmRows.next()) {
            String sq = "SELECT * FROM genres WHERE id = ?";
            log.info("Жанр с id={} получен",id);
            return jdbcTemplate.queryForObject(sq, genreRowMapper(), id);
        } else {
            throw new EntityNotFoundException(Genres.class);
        }
    }

    private RowMapper<Genres> genreRowMapper() {
        return (rs, rowNum) -> new Genres(rs.getInt("id"),
                rs.getString("name_genre"));
    }
}
