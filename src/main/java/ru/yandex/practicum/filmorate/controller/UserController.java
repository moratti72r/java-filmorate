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
import java.util.List;

@RequestMapping ("/users")
@RestController
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger("UserController.class");

    private final List<User> users = new ArrayList<>();

    private int idGenerator = 0;

    @GetMapping()
    public ResponseEntity<?> findAll (){
        log.info("Получен GET запрос.");
        return !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping()
    public ResponseEntity<?> create (@RequestBody @Valid User user){
        if (user.getName()==null || user.getName().isEmpty() ){
            user.setName(user.getLogin());
        }
        idGenerator++;
        user.setId(idGenerator);
        users.add(user);
        log.info("Пользователь добавлен");
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }


    @PutMapping()
    public ResponseEntity<?> upDate (@RequestBody @Valid User user){
        if (user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        if (!users.isEmpty()) {
            boolean cont = true;
            for (User userFromList : users) {
                if (userFromList.getId()==user.getId()) {
                    cont = false;
                    users.remove(userFromList);
                    users.add(user);
                    log.info("Пользователь с id "+user.getId()+" изменен");
                    break;
                }
            }
            if (cont){
                log.warn("Пользователь с id "+user.getId()+" отсутствует");
                return new ResponseEntity<> (user,HttpStatus.NOT_FOUND);
            }
        }else {
            log.warn("Пользователи отсутствуют");
            return new ResponseEntity<> (user,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<> (user,HttpStatus.OK);
    }

}
