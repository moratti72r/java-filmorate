package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenreDao {
    List<Genres> getAll();

    Genres getById(Integer id);
}
