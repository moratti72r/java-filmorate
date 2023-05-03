package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.Min;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        log.info("Список пользователей получен");
        return jdbcTemplate.query("SELECT * FROM mov_users", userRowMapper());
    }

    @Override
    public User create(User user) {

        String sqlQuery = "INSERT INTO mov_users (email," +
                "login," +
                "name," +
                "birthday) " +
                "VALUES (?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement psst = connection.prepareStatement(sqlQuery, new String[]{"id"});
            psst.setString(1, user.getEmail());
            psst.setString(2, user.getLogin());
            psst.setString(3, user.getName());
            psst.setDate(4, Date.valueOf(user.getBirthday()));
            return psst;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());

        log.info("Пользователь с id=" + user.getId() + " добавлен");
        return user;
    }

    @Override
    public User upDate(User user) {
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("SELECT * FROM mov_users WHERE id = ?", user.getId());
        if (usersRows.next()) {
            String sqlQuery = "UPDATE mov_users SET " +
                    "email = ?," +
                    "login = ?," +
                    "name = ?," +
                    "birthday = ?" +
                    " WHERE id = ?";

            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());

            log.info("Пользователь успешно изменен");
            return user;
        } else {
//            log.warn("Пользователь с id " + user.getId() + " отсутствует");
            throw new EntityNotFoundException(UserDbStorage.class);
        }
    }

    @Override
    public User getById(Integer id) {
        if (contains(id)) {
            String sql = "SELECT * FROM mov_users WHERE id = ?";
            User user = jdbcTemplate.queryForObject(sql, userRowMapper(), id );
            log.info("Пользователь с id " + id + " получен");
            return user;
        } else {
//            log.warn("Пользователь с id " + id + " отсутствует");
            throw new EntityNotFoundException(UserDbStorage.class);
        }
    }

    @Override
    public boolean contains(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select 1 from mov_users where id = ?", id);
        return userRows.next();
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        if (contains(id)) {

            String sql = "SELECT * FROM mov_users WHERE id IN (SELECT id_friend FROM friends WHERE id_user = ?)";

            log.info("Список друзей пользователя " + id + " получен");
            return jdbcTemplate.query(sql, userRowMapper(), id);
        } else {
//            log.warn("Пользователь с id " + id + " отсутствует");
            throw new EntityNotFoundException(UserDbStorage.class);
        }
    }

    @Override
    public List<User> getMutualFriends(Integer idUser, Integer otherIdUser) {
        if (idUser.equals(otherIdUser)) {
            log.warn("Значения не должны быть одинаковыми");
            throw new IncorrectArgumentsException(UserDbStorage.class);
        }
        if (!contains(idUser)) {
            log.warn("Пользователь с id " + idUser + " отсутствует");
            throw new EntityNotFoundException(UserDbStorage.class);
        }
        if (!contains(otherIdUser)) {
            log.warn("Пользователь с id " + otherIdUser + " отсутствует");
            throw new EntityNotFoundException(UserDbStorage.class);
        }

        String sqlQuery = "SELECT * FROM mov_users WHERE id IN (SELECT id_friend FROM friends " +
                "WHERE id_user=" + otherIdUser + " AND id_friend IN (SELECT id_friend " +
                "FROM friends " +
                "WHERE id_user =" + idUser + "))";

        log.info("Список общих друзей пользователей получен");
        return jdbcTemplate.query(sqlQuery, userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());

            return user;
        };
    }

    private RowMapper<Integer> mapper(String nameColumn) {
        return (rs, rowNum) -> rs.getInt(nameColumn);
    }

}
