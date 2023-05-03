package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.timevalidator.TimeAfterDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {

    private int id;

    @NotBlank
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    @TimeAfterDate(date = "28.12.1895")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Min(value = 1)
    private int duration;

    @JsonIgnore
    private int likes;

    Set<Genres> genres = new TreeSet<>();

    @NotNull
    MPA mpa;

}
