package ru.yandex.practicum.filmorate.dao.storageimpl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genres> getAll() {
        String sq = "SELECT * FROM genres";
        return jdbcTemplate.query(sq, genreRowMapper());
    }

    @Override
    public Genres getById(int id) {

        String sq = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.queryForObject(sq, genreRowMapper(), id);

    }

    @Override
    public boolean contains(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from genres where id = ?", id);
        return filmRows.next();
    }

    private RowMapper<Genres> genreRowMapper() {
        return (rs, rowNum) -> new Genres(rs.getInt("id"),
                rs.getString("name_genre"));
    }
}
