package ru.yandex.practicum.filmorate.validators.durationvalidator;

import ru.yandex.practicum.filmorate.validators.timevalidator.TimeAfterDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class DurationIsPositiveValidator implements ConstraintValidator<DurationIsPositive, Duration> {

    @Override
    public boolean isValid(Duration target, ConstraintValidatorContext context) {
        return target.getSeconds()>0;
    }
}
