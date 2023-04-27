package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
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

    private int idGenerator = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        log.info("Список пользователей получен");
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        idGenerator++;
        user.setId(idGenerator);
        users.put(idGenerator, user);
        log.info("Пользователь с id=" + user.getId() + " добавлен");
        return user;
    }

    @Override
    public User upDate(User user) {
        if (contains(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь успешно изменен");
        } else {
            log.warn("Пользователь с id " + user.getId() + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + user.getId() + " отсутствует");
        }
        return user;
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
            for (Integer idFriend : getById(id).getFriends()) {
                allFriends.add(getById(idFriend));
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

