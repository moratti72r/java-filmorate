package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MPA> getAll() {
        String sq = "SELECT * FROM mpa";
        log.info("Список возрастных рейтингов получен");
        return jdbcTemplate.query(sq, mpaRowMapper());
    }

    @Override
    public MPA getById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from mpa where id = ?", id);
        if (filmRows.next()) {
            String sq = "SELECT * FROM mpa WHERE id = ?";
            log.info("Возрастной рейтинг с id={} получен", id);
            return jdbcTemplate.queryForObject(sq, mpaRowMapper(), id);
        } else {
            throw new EntityNotFoundException(MPA.class);
        }
    }

    private RowMapper<MPA> mpaRowMapper() {
        return (rs, rowNum) -> {
            MPA mpa = new MPA();
            mpa.setId(rs.getInt("id"));
            mpa.setName(rs.getString("name_mpa"));
            return mpa;
        };
    }
}
