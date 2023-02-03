package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @Getter
    private final UserStorage userStorage;


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
        userStorage.getById(idUser).getFriends().add(idFriend);
        userStorage.getById(idFriend).getFriends().add(idUser);

        log.info("Пользователь успешно добавлен в друзья");

        return userStorage.getById(idUser);
    }

    public User removeToFriends(Integer idUser, Integer idFriend) {
        if (userStorage.getById(idUser).getFriends().contains(idFriend)) {
            userStorage.getById(idFriend).getFriends().remove(idUser);
            userStorage.getById(idUser).getFriends().remove(idFriend);

            log.info("Пользователь успешно удален из друзей");

            return userStorage.getById(idUser);
        } else {

            log.warn("Пользователь с id " + idFriend + " отсутствует");

            throw new UserNotFoundException("Пользователь с id " + idFriend + " отсутствует");
        }
    }

    public List<User> findAllFriends(Integer id) {
        if (userStorage.contains(id)) {
            ArrayList<User> allFriends = new ArrayList<>();
            for (Integer idFriends : userStorage.getById(id).getFriends()) {
                allFriends.add(userStorage.getById(idFriends));
            }

            log.info("Список друзей пользователя " + id + " получен");

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
        if (!userStorage.contains(idUser)) {
            log.warn("Пользователь с id " + idUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + idUser + " отсутствует");
        }
        if (!userStorage.contains(otherIdUser)) {
            log.warn("Пользователь с id " + otherIdUser + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + otherIdUser + " отсутствует");
        }

        ArrayList<User> mutualFriends = new ArrayList<>();
        for (Integer id : userStorage.getById(idUser).getFriends()) {
            if (userStorage.getById(otherIdUser).getFriends().contains(id)) {
                mutualFriends.add(userStorage.getById(id));
            }
        }
        log.info("Список общих друзей пользователей получен");
        return mutualFriends;
    }


}
