package com.example.blog.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidatorImpl.class)
@Documented
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid value. Must be one of the defined enum values.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
