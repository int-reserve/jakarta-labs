package edu.kpi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ValidSessionTimeValidator implements
    ConstraintValidator<ValidSessionTime, LocalDateTime> {

  @Override
  public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    if (value.isBefore(LocalDateTime.now())) {
      return false;
    }

    int minute = value.getMinute();
    return minute == 0 || minute == 30;
  }
}
