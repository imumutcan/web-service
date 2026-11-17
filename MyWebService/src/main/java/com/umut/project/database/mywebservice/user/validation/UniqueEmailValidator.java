package com.umut.project.database.mywebservice.user.validation;

import com.umut.project.database.mywebservice.user.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return !userRepository.existsByEmail(value);
    }
}
