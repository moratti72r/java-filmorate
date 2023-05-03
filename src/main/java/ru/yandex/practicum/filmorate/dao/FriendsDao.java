package ru.yandex.practicum.filmorate.dao;

import org.springframework.http.ResponseEntity;

public interface FriendsDao {
    ResponseEntity<?> addToFriends(int idUser, int idFriend);
    ResponseEntity<?> removeToFriends (int idUser, int idFriend);
}
