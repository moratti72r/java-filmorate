package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger("UserService.class");

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addToFriends(Integer idUser, Integer idFriend) {
        if (idUser.equals(idFriend)) {
            log.warn("Значения не должны быть одинаковыми");
            throw new IncorrectArgumentsException("Значения не должны быть одинаковыми");
        }
        if (!userStorage.findAll().containsKey(idUser)) {
            log.warn("Пользователь с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + idUser + " отсутствует");
        }
        if (!userStorage.findAll().containsKey(idFriend)) {
            log.warn("Пользователь с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + idFriend + " отсутствует");
        }
        userStorage.findAll().get(idUser).getFriends().add(idFriend);
        userStorage.findAll().get(idFriend).getFriends().add(idUser);

        log.info("Пользователь успешно добавлен в друзья");

        return userStorage.findAll().get(idUser);
    }

    public User removeToFriends(Integer idUser, Integer idFriend) {
        if (userStorage.findAll().get(idUser).getFriends().contains(idFriend)) {
            userStorage.findAll().get(idFriend).getFriends().remove(idUser);
            userStorage.findAll().get(idUser).getFriends().remove(idFriend);

            log.info("Пользователь успешно удален из друзей");

            return userStorage.findAll().get(idUser);
        } else {

            log.warn("Пользователь с id " + idFriend + " отсутствует");

            throw new UserNotFoundException("Пользователь с id " + idFriend + " отсутствует");
        }
    }

    public List<User> findAllFriends(Integer id) {
        if (userStorage.findAll().containsKey(id)) {
            ArrayList<User> allFriends = new ArrayList<>();
            for (Integer idFriends : userStorage.findAll().get(id).getFriends()) {
                allFriends.add(userStorage.findAll().get(idFriends));
            }

            log.info("Список друзей пользователя получен");

            return allFriends;
        } else {

            log.warn("Пользователь с id " + id + " отсутствует");

            throw new UserNotFoundException("Пользователь с id " + id + " отсутствует");
        }
    }

    public List<User> findMutualFriends(Integer idUser, Integer otherIdUser) {
        if (idUser.equals(otherIdUser)) {
            log.warn("Значения не должны быть одинаковыми");
            throw new IncorrectArgumentsException("Значения не должны быть одинаковыми");
        }
        if (!userStorage.findAll().containsKey(idUser)) {
            log.warn("Пользователь с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + idUser + " отсутствует");
        }
        if (!userStorage.findAll().containsKey(otherIdUser)) {
            log.warn("Пользователь с id " + otherIdUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + otherIdUser + " отсутствует");
        }

        ArrayList<User> mutualFriends = new ArrayList<>();
        for (Integer id : userStorage.findAll().get(idUser).getFriends()) {
            if (userStorage.findAll().get(otherIdUser).getFriends().contains(id)) {
                mutualFriends.add(userStorage.findAll().get(id));
            }
        }
        log.info("Список общих друзей пользователей получен");
        return mutualFriends;
    }


}
