package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private int idGenerator = 0;

    private final UserStorage userStorage;

    public List<User> findAll() {
        return new ArrayList<User>(userStorage.getAll().values());
    }

    public User findById(Integer id) {
        return userStorage.getById(id);
    }

    public List<User> findAllFriends(Integer id) {
        return userStorage.getAllFriends(id);
    }

    public List<User> findMutualFriends(Integer idUser, Integer otherIdUser) {
        return userStorage.getMutualFriends(idUser, otherIdUser);
    }

    public User addToFriends(Integer idUser, Integer idFriend) {
        if (idUser.equals(idFriend)) {
            log.warn("Значения не должны быть одинаковыми");
            throw new IncorrectArgumentsException("Значения не должны быть одинаковыми");
        }
        if (!userStorage.contains(idUser)) {
            log.warn("Пользователь с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + idUser + " отсутствует");
        }
        if (!userStorage.contains(idFriend)) {
            log.warn("Пользователь с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + idFriend + " отсутствует");
        }
        findById(idUser).getFriends().add(idFriend);
        findById(idFriend).getFriends().add(idUser);

        log.info("Пользователь успешно добавлен в друзья");

        return findById(idUser);
    }

    public User removeToFriends(Integer idUser, Integer idFriend) {
        if (findById(idUser).getFriends().contains(idFriend)) {
            findById(idFriend).getFriends().remove(idUser);
            findById(idUser).getFriends().remove(idFriend);

            log.info("Пользователь успешно удален из друзей");

            return findById(idUser);
        } else {

            log.warn("Пользователь с id " + idFriend + " отсутствует");

            throw new UserNotFoundException("Пользователь с id " + idFriend + " отсутствует");
        }
    }

    public User create(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        idGenerator++;
        user.setId(idGenerator);
        userStorage.getAll().put(idGenerator, user);
        log.info("Пользователь добавлен");
        return user;
    }

    public User upDate(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        if (userStorage.contains(user.getId())) {
            userStorage.getAll().put(user.getId(), user);
            log.info("Пользователь изменен");
        } else {
            log.warn("Пользователь с id " + user.getId() + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + user.getId() + " отсутствует");
        }
        return user;
    }


}
