package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RequestMapping("/mpa")
@RestController
@RequiredArgsConstructor
public class MPAController {

    private final MpaService mpaService;

    @GetMapping()
    public List<MPA> findAllMpa() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public MPA getByIdMpa(@PathVariable Integer id) {
        return mpaService.findById(id);
    }
}
