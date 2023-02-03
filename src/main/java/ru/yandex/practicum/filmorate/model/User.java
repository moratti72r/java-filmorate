package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;

    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    private Set<Integer> likes = new HashSet<>();

}
