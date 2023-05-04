package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenreStorage {
    List<Genres> getAll();

    Genres getById(int id);

    boolean contains(int id);
}
