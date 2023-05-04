package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.storageimpl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectArgumentsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    public List<User> findAll() {
        log.info("Список пользователей получен");
        return userStorage.getAll();
    }

    public User findById(int id) {
        if (userStorage.contains(id)) {
            log.info("Пользователь с id={} получен", id);
            return userStorage.getById(id);
        } else {
            throw new EntityNotFoundException(UserDbStorage.class);
        }
    }

    public List<User> findAllFriends(int id) {
        if (userStorage.contains(id)) {
            log.info("Список друзей пользователя c id={} получен", id);
            return userStorage.getAllFriends(id);
        } else {
            throw new EntityNotFoundException(UserDbStorage.class);
        }
    }

    public List<User> findMutualFriends(int idUser, int otherIdUser) {
        if (idUser == otherIdUser) {
            throw new IncorrectArgumentsException(UserService.class);
        }
        if (!userStorage.contains(idUser)) {
            throw new EntityNotFoundException(UserService.class);
        }
        if (!userStorage.contains(otherIdUser)) {
            throw new EntityNotFoundException(UserService.class);
        }
        log.info("Список общих друзей пользователей c id={} и c id={} получен", idUser, otherIdUser);
        return userStorage.getMutualFriends(idUser, otherIdUser);
    }

    public void addToFriends(int idUser, int idFriend) {
        if (!userStorage.contains(idUser) || !userStorage.contains(idFriend)) {
            throw new EntityNotFoundException(UserService.class);
        }

        int result = friendsDao.addToFriends(idUser, idFriend);

        if (idUser == idFriend || result == 0) {
            throw new IncorrectArgumentsException(UserService.class);
        }

        log.info("Пользователь c id={} успешно добавлен в друзья пользователю c id={}", idFriend, idUser);
    }

    public void removeToFriends(int idUser, int idFriend) {

        if (!userStorage.contains(idUser) || !userStorage.contains(idFriend)) {
            throw new EntityNotFoundException(UserService.class);
        }

        int result = friendsDao.removeToFriends(idUser, idFriend);

        if (idUser == idFriend || result == 0) {
            throw new IncorrectArgumentsException(UserService.class);
        }

        log.info("Пользователь c id={} успешно удален из друзей пользователя c id={}", idFriend, idUser);

    }

    public User create(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        log.info("Пользователь с id={} добавлен", user.getId());
        return userStorage.create(user);
    }

    public User upDate(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }

        int result = userStorage.upDate(user);

        if (result == 0) {
            throw new EntityNotFoundException(UserService.class);
        }
        log.info("Пользователь c id={} успешно изменен", user.getId());
        return user;
    }
}
