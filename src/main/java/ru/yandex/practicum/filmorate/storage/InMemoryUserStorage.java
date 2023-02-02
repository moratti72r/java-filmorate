package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static final Logger log = LoggerFactory.getLogger("InMemoryUserStorage.class");

    private final Map<Integer, User> users = new HashMap<>();

    private int idGenerator = 0;

    @Override
    public Map<Integer, User> findAll() {
        log.info("Вывод списка пользователей");
        return users;
    }

    @Override
    public User getById(Integer id) {
        if (findAll().containsKey(id)) {
            log.info("Получен GET запрос");
            return findAll().get(id);
        } else {
            log.warn("Пользователь с id " + id + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + id + " отсутствует");
        }
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        idGenerator++;
        user.setId(idGenerator);
        users.put(idGenerator, user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User upDate(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь изменен");
        } else {
            log.warn("Пользователь с id " + user.getId() + " отсутствует");
            throw new UserNotFoundException("Пользватель с id " + user.getId() + " отсутствует");
        }

        return user;
    }
}

