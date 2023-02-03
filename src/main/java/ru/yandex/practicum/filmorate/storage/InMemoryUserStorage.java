package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int idGenerator = 0;

    @Override
    public Map<Integer, User> findAll() {
        log.info("Вывод списка пользователей");
        return users;
    }

    @Override
    public boolean contains(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public User getById(Integer id) {
        if (findAll().containsKey(id)) {
            log.info("Пользователь с id " + id + " получен");
            return findAll().get(id);
        } else {
            log.warn("Пользователь с id " + id + " отсутствует");
            throw new UserNotFoundException("Пользователь с id " + id + " отсутствует");
        }
    }

    @Override
    public User create(User user) {
        if (StringUtils.isEmpty(user.getName())) {
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
        if (StringUtils.isEmpty(user.getName())) {
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

