package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/films")
@RestController
@Validated
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger("FilmController.class");

    private List<Film> films = new ArrayList<>();

    private int idGenerator = 0;

    @GetMapping()
    public ResponseEntity<?> findAll(){
        log.info("Получен GET запрос.");
        return !films.isEmpty()
                ? new ResponseEntity<>(films, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping ()
    public ResponseEntity<?> create (@RequestBody @Valid Film film){
        idGenerator++;
        film.setId(idGenerator);
        films.add(film);
        log.info("Фильм добавлен");
        return new ResponseEntity<>(film,HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<?> upDate (@RequestBody @Valid Film film){
        if (!films.isEmpty()) {
            boolean isCont = true;
            for (Film filmFromList : films) {
                if (filmFromList.getId()==film.getId()) {
                    isCont = false;
                    films.remove(filmFromList);
                    films.add(film);
                    log.info("Фильм с id "+film.getId()+" изменен");
                    break;
                }
            }if (isCont){
                log.warn("Фильм с id "+film.getId()+" отсутствует");
                return new ResponseEntity<> (film,HttpStatus.NOT_FOUND);
            }
        }else {
            log.warn("Фильмы отсутствуют");
            return new ResponseEntity<> (film,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (film,HttpStatus.OK);
        }
    }

