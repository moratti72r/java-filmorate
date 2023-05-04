package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaStorage {

    List<MPA> getAll();

    MPA getById(int id);

    boolean contains(int id);
}
