package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id_user"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());

            String sqFriends = "SELECT id_friend FROM friends WHERE id_user=?";
            user.setFriends(new HashSet<>(jdbcTemplate.query(sqFriends, mapper("id_friend"), user.getId())));

            String sqLikes = "SELECT id_film FROM likes WHERE id_user=?";
            user.setLikesOnMovies(new HashSet<>(jdbcTemplate.query(sqLikes, mapper("id_film"), user.getId())));

            return user;
        };
    }

    private RowMapper<Integer> mapper(String nameColumn) {
        return (rs, rowNum) -> {
            return rs.getInt(nameColumn);
        };
    }

    private void addFriendsAndLikes(User user) {
        if (!user.getFriends().isEmpty()) {
            String sq = "INSERT INTO friends (id_user," +
                    "id_friend) " +
                    "VALUES (?,?)";
            for (Integer id : user.getFriends()) {
                jdbcTemplate.update(sq, user.getId(), id);
            }
        }

        if (!user.getLikesOnMovies().isEmpty()) {
            String sq = "INSERT INTO likes (id_user," +
                    "id_film) " +
                    "VALUES (?,?)";
            for (Integer id : user.getLikesOnMovies()) {
                jdbcTemplate.update(sq, user.getId(), id);
            }
        }
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM mov_users";
        log.info("Список пользователей получен");
        return jdbcTemplate.query(sqlQuery, userRowMapper());
    }

    @Override
    public User create(User user) {

        String sqlQuery = "INSERT INTO mov_users (email," +
                "login," +
                "name," +
                "birthday) " +
                "VALUES (?,?,?,?)";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        int idUser = jdbcTemplate.queryForObject("SELECT id_user FROM mov_users WHERE name = ?", Integer.class, user.getName());
        user.setId(idUser);

        addFriendsAndLikes(user);

        log.info("Пользователь с id=" + user.getId() + " добавлен");
        return user;
    }

    @Override
    public User upDate(User user) {
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("SELECT * FROM mov_users WHERE id_user = ?", user.getId());
        if (usersRows.next()) {
            String sqlQuery = "UPDATE mov_users SET " +
                    "email = ?," +
                    "login = ?," +
                    "name = ?," +
                    "birthday = ?" +
                    " WHERE id_user = ?";

            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());

            String sqDelFr = "DELETE FROM friends WHERE id_user = ?";
            jdbcTemplate.update(sqDelFr, user.getId());

            String sqDelLik = "DELETE FROM likes WHERE id_user = ?";
            jdbcTemplate.update(sqDelLik, user.getId());

            addFriendsAndLikes(user);

            log.info("Пользователь успешно изменен");
            return user;
        } else {
            log.warn("Пользователь с id " + user.getId() + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + user.getId() + " отсутствует");
        }
    }

    @Override
    public User getById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM mov_users WHERE id_user = ?", id);
        if (userRows.next()) {
            User user = new User();

            user.setId(id);
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(userRows.getDate("birthday").toLocalDate());

            String sqFriends = "SELECT id_friend FROM friends WHERE id_user=?";
            user.setFriends(new HashSet<>(jdbcTemplate.query(sqFriends, mapper("id_friend"), user.getId())));

            String sqLikes = "SELECT id_film FROM likes WHERE id_user=?";
            user.setLikesOnMovies(new HashSet<>(jdbcTemplate.query(sqLikes, mapper("id_film"), user.getId())));

            log.info("Пользователь с id " + id + " получен");
            return user;
        } else {
            log.warn("Пользователь с id " + id + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + id + " отсутствует");
        }
    }

    @Override
    public boolean contains(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from mov_users where id_user = ?", id);
        return userRows.next();
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        if (contains(id)) {
            String sqlQuery = "SELECT * FROM mov_users " +
                    "WHERE id_user IN (SELECT id_friend " +
                    "FROM friends " +
                    "WHERE id_user = ?)";

            log.info("Список друзей пользователя " + id + " получен");
            return jdbcTemplate.query(sqlQuery, userRowMapper(), id);
        } else {
            log.warn("Пользователь с id " + id + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + id + " отсутствует");
        }
    }

    @Override
    public List<User> getMutualFriends(Integer idUser, Integer otherIdUser) {
        if (idUser.equals(otherIdUser)) {
            log.warn("Значения не должны быть одинаковыми");
            throw new IncorrectArgumentsException("Значения не должны быть одинаковыми");
        }
        if (!contains(idUser)) {
            log.warn("Пользователь с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + idUser + " отсутствует");
        }
        if (!contains(otherIdUser)) {
            log.warn("Пользователь с id " + otherIdUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + otherIdUser + " отсутствует");
        }

        String sqlQuery = "SELECT * FROM mov_users WHERE id_user IN (SELECT id_friend FROM friends " +
                "WHERE id_user=" + otherIdUser + " AND id_friend IN (SELECT id_friend " +
                "FROM friends " +
                "WHERE id_user =" + idUser + "))";

        log.info("Список общих друзей пользователей получен");
        return jdbcTemplate.query(sqlQuery, userRowMapper());
    }

}
