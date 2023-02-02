package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> findAll();

    public Film getById(Integer id);

    Film create(Film film);

    Film upDate(Film film);
}
