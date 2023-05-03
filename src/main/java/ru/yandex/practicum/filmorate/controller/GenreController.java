package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

@RequestMapping("/genres")
@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreDao genreDao;

    @GetMapping()
    public List<Genres> findAllGenres() {
        return genreDao.getAll();
    }

    @GetMapping("/{id}")
    public Genres getGenresById(@PathVariable Integer id) {
        return genreDao.getById(id);
    }
}
