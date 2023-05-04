package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmStorage {

    List<Film> getAll();

    Film create(Film film);

    int upDate(Film film);

    boolean contains(int id);

    List<Film> getTopFilms(int limit);

    Film getById(int id);


}
