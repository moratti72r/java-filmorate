package ru.yandex.practicum.filmorate.dao.storageimpl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MPA> getAll() {
        String sq = "SELECT * FROM mpa";
        return jdbcTemplate.query(sq, mpaRowMapper());
    }

    @Override
    public MPA getById(int id) {
        String sq = "SELECT * FROM mpa WHERE id = ?";
        return jdbcTemplate.queryForObject(sq, mpaRowMapper(), id);

    }

    @Override
    public boolean contains(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from mpa where id = ?", id);
        return filmRows.next();
    }

    private RowMapper<MPA> mpaRowMapper() {
        return (rs, rowNum) -> new MPA(rs.getInt("id"),
                rs.getString("name_mpa"));
    }
}
