package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
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

    public ResponseEntity<?> addToFriends(Integer idUser, Integer idFriend) {

        return friendsDao.addToFriends(idUser, idFriend);
    }

    public ResponseEntity<?> removeToFriends(Integer idUser, Integer idFriend) {
        return friendsDao.removeToFriends(idUser, idFriend);
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
