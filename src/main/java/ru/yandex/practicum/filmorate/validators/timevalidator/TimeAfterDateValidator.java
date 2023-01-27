package ru.yandex.practicum.filmorate.validators.timevalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeAfterDateValidator implements ConstraintValidator <TimeAfterDate, LocalDate> {

    private LocalDate annotationDate;

    @Override
    public void initialize(TimeAfterDate constraintAnnotation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.annotationDate = LocalDate.parse(constraintAnnotation.date(),formatter);
    }

    @Override
    public boolean isValid(LocalDate target, ConstraintValidatorContext context) {
        return target.isAfter(annotationDate);
    }

}
