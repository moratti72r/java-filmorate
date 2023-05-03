package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@RequestMapping("/mpa")
@RestController
@RequiredArgsConstructor
public class MPAController {

    private final MpaDaoImpl mpaDao;

    @GetMapping()
    public List<MPA> findAllMpa() {
        return mpaDao.getAll();
    }

    @GetMapping("/{id}")
    public MPA getByIdMpa(@PathVariable Integer id) {
        return mpaDao.getById(id);
    }
}
