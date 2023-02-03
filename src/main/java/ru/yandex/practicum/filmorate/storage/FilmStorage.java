package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> findAll();

    boolean contains(Integer id);

    Film getById(Integer id);

    Film create(Film film);

    Film upDate(Film film);
}
