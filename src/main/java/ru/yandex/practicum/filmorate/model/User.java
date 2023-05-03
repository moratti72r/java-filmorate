package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {

    private int id;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{6,12}$")
    private String login;

    private String name;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate birthday;

}
