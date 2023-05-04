package ru.yandex.practicum.filmorate.dao;

public interface FriendsDao {

    int addToFriends(int idUser, int idFriend);

    int removeToFriends(int idUser, int idFriend);

}
