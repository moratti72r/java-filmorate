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


    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.getAll();
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
        User user = findById(idUser);
        User friend = findById(idFriend);
        user.getFriends().add(friend.getId());

        log.info("Пользователь успешно добавлен в друзья");

        return userStorage.upDate(user);
    }

    public User removeToFriends(Integer idUser, Integer idFriend) {
        User user = findById(idUser);
        User friend = findById(idFriend);
        if (user.getFriends().contains(idFriend)) {
            user.getFriends().remove(idFriend);

            if (friend.getFriends().contains(idUser)) {
                friend.getFriends().remove(idUser);
            }
            log.info("Пользователь успешно удален из друзей");

            userStorage.upDate(friend);
            return userStorage.upDate(user);
        } else {
            log.warn("Пользователь с id " + idFriend + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + idFriend + " отсутствует");
        }
    }

    public User create(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User upDate(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        return userStorage.upDate(user);
    }


}
