package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> findAll();

    boolean contains(Integer id);

    List<Film> getSortFilms();

    Film getById(Integer id);

    Film create(Film film);

    Film upDate(Film film);
}
