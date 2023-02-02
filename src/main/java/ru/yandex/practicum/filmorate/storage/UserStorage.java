package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    Map<Integer, User> findAll();

    User getById(Integer id);

    User create(User user);

    User upDate(User user);

}
