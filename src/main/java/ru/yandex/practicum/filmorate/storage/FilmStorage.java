package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getAll();

    boolean contains(Integer id);

    List<Film> getTopFilms(int limit);

    Film getById(Integer id);

}
