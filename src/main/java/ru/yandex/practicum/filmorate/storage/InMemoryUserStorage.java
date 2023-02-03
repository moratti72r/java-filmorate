package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Map<Integer, User> getAll() {
        log.info("Вывод списка пользователей");
        return users;
    }

    @Override
    public boolean contains(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public User getById(Integer id) {
        if (contains(id)) {
            log.info("Пользователь с id " + id + " получен");
            return users.get(id);
        } else {
            log.warn("Пользователь с id " + id + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + id + " отсутствует");
        }
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        if (contains(id)) {
            ArrayList<User> allFriends = new ArrayList<>();
            for (Integer idFriends : getById(id).getFriends()) {
                allFriends.add(getById(idFriends));
            }

            log.info("Список друзей пользователя " + id + " получен");

            return allFriends;
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

        ArrayList<User> mutualFriends = new ArrayList<>();
        for (Integer id : getById(idUser).getFriends()) {
            if (getById(otherIdUser).getFriends().contains(id)) {
                mutualFriends.add(getById(id));
            }
        }
        log.info("Список общих друзей пользователей получен");
        return mutualFriends;
    }
}

