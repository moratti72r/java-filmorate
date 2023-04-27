package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User create(User user);

    User upDate(User user);

    User getById(Integer id);

    boolean contains(Integer id);

    List<User> getAllFriends(Integer id);

    List<User> getMutualFriends(Integer idUser, Integer otherIdUser);
}
