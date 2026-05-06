package edu.kpi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidSessionTimeValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSessionTime {
    String message() default "Session time must be in the future and strictly on the hour or half-hour (e.g. 18:00 or 18:30)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
