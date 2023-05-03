package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.storage.UserStorage;


@Slf4j
@Component
@AllArgsConstructor
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Override
    public ResponseEntity<?> addToFriends(int idUser, int idFriend) {
        if (idUser == idFriend || contains(idUser, idFriend)) {
            throw new IncorrectArgumentsException(FriendsDaoImpl.class);
        }
        if (!userStorage.contains(idUser) || !userStorage.contains(idFriend)) {
            throw new EntityNotFoundException(FriendsDaoImpl.class);
        }

        jdbcTemplate.update("INSERT INTO friends (id_user,id_friend) VALUES (?,?)", idUser, idFriend);
        log.info("Пользователь c id={} успешно добавлен в друзья пользователю c id={}", idFriend, idUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeToFriends(int idUser, int idFriend) {
        if (idUser == idFriend || !contains(idUser, idFriend)) {
            throw new IncorrectArgumentsException(FriendsDaoImpl.class);
        }

        if (!userStorage.contains(idUser) || !userStorage.contains(idFriend)) {
            throw new EntityNotFoundException(FriendsDaoImpl.class);
        }

        jdbcTemplate.update("DELETE FROM friends WHERE id_user = ? AND id_friend = ?", idUser, idFriend);
        log.info("Пользователь c id={} успешно удален из друзей пользователя c id={}", idFriend, idUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean contains(int idUser, int idFriend) {
        SqlRowSet friendsRow = jdbcTemplate.queryForRowSet("SELECT 1 FROM friends " +
                        "WHERE id_user = ? AND id_friend = ?",
                idUser, idFriend);
        return friendsRow.next();
    }

}
