package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TeacherTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenTeacherIsValid_thenReturnNoConstraintViolations() {
        Teacher teacher = new Teacher()
                .setLastName("Doe")
                .setFirstName("John")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenLastNameIsBlank_thenReturnConstraintViolation() {
        Teacher teacher = new Teacher()
                .setLastName("")
                .setFirstName("John")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("ne doit pas être vide");
    }

    @Test
    public void whenFirstNameIsBlank_thenReturnConstraintViolation() {
        Teacher teacher = new Teacher()
                .setLastName("Doe")
                .setFirstName("")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("ne doit pas être vide");
    }

    @Test
    public void whenLastNameExceedsMaxLength_thenReturnConstraintViolation() {
        Teacher teacher = new Teacher()
                .setLastName("ThisIsAReallyLongLastNameThatExceedsTheMaximumAllowedLength")
                .setFirstName("John")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("la taille doit être comprise entre 0 et 20");
    }

    @Test
    public void whenFirstNameExceedsMaxLength_thenReturnConstraintViolation() {
        Teacher teacher = new Teacher()
                .setLastName("Doe")
                .setFirstName("ThisIsAReallyLongFirstNameThatExceedsTheMaximumAllowedLength")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("la taille doit être comprise entre 0 et 20");
    }
}
