package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> findAll() {
        return new ArrayList<User>(userService.getUserStorage().findAll().values());
    }


    @PostMapping()
    public User create(@RequestBody @Valid User user) {
        return userService.getUserStorage().create(user);
    }


    @PutMapping()
    public User upDate(@RequestBody @Valid User user) {
        return userService.getUserStorage().upDate(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
        return userService.getUserStorage().getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.removeToFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable Integer id) {
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.findMutualFriends(id, otherId);
    }

}
