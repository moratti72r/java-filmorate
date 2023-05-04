package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<MPA> findAll() {
        log.info("Список возрастных рейтингов получен");
        return mpaStorage.getAll();
    }

    public MPA findById(int id) {
        if (mpaStorage.contains(id)) {
            log.info("Возрастной рейтинг с id={} получен", id);
            return mpaStorage.getById(id);
        } else {
            throw new EntityNotFoundException(MpaService.class);
        }
    }
}
