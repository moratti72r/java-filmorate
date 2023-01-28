package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger("UserController.class");

    private final Map<Integer, User> users = new HashMap<>();

    private int idGenerator = 0;

    @GetMapping()
    public ResponseEntity<?> findAll() {
        log.info("Получен GET запрос.");
        return new ResponseEntity<>(users.values(), HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        idGenerator++;
        user.setId(idGenerator);
        users.put(idGenerator, user);
        log.info("Пользователь добавлен");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PutMapping()
    public ResponseEntity<?> upDate(@RequestBody @Valid User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!users.isEmpty()) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Пользователь с id " + user.getId() + " изменен");
            } else {
                log.warn("Пользователь с id " + user.getId() + " отсутствует");
                return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
            }
        } else {
            log.warn("Пользователи отсутствуют");
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
