package ru.yandex.practicum.filmorate.dao.storageimpl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
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

        return user;
    }

    @Override
    public int upDate(User user) {
        String sqlQuery = "UPDATE mov_users SET " +
                "email = ?," +
                "login = ?," +
                "name = ?," +
                "birthday = ?" +
                " WHERE id = ?";

        int result = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return result;
    }


    @Override
    public User getById(int id) {
        String sql = "SELECT * FROM mov_users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sql, userRowMapper(), id);
        return user;

    }

    @Override
    public boolean contains(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select 1 from mov_users where id = ?", id);
        return userRows.next();
    }

    @Override
    public List<User> getAllFriends(int id) {

        String sql = "SELECT * FROM mov_users WHERE id IN (SELECT id_friend FROM friends WHERE id_user = ?)";

        return jdbcTemplate.query(sql, userRowMapper(), id);
    }

    @Override
    public List<User> getMutualFriends(int idUser, int otherIdUser) {

        String sqlQuery = "SELECT * FROM mov_users WHERE id IN (SELECT id_friend FROM friends " +
                "WHERE id_user=" + otherIdUser + " AND id_friend IN (SELECT id_friend " +
                "FROM friends " +
                "WHERE id_user =" + idUser + "))";

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
}
