package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;


@Component
@AllArgsConstructor
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int addToFriends(int idUser, int idFriend) {

        return jdbcTemplate.update("INSERT INTO friends (id_user,id_friend) VALUES (?,?)", idUser, idFriend);

    }

    @Override
    public int removeToFriends(int idUser, int idFriend) {

        return jdbcTemplate.update("DELETE FROM friends WHERE id_user = ? AND id_friend = ?", idUser, idFriend);


    }

}
