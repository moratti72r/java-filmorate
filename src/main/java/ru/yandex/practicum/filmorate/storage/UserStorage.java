package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User create(User user);

    int upDate(User user);

    User getById(int id);

    boolean contains(int id);

    List<User> getAllFriends(int id);

    List<User> getMutualFriends(int idUser, int otherIdUser);
}
