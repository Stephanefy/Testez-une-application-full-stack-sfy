package com.openclassrooms.starterjwt.models;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class UserTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenUserIsValid_thenReturnNoConstraintViolations() {
        User user = new User()
                .setEmail("john.doe@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("StrongPassword123")
                .setAdmin(true)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenEmailIsInvalid_thenReturnConstraintViolation() {
        User user = new User()
                .setEmail("invalid-email")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("StrongPassword123")
                .setAdmin(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("doit être une adresse électronique syntaxiquement correcte");
    }

    @Test
    public void whenLastNameExceedsMaxLength_thenReturnConstraintViolation() {
        User user = new User()
                .setEmail("john.doe@example.com")
                .setFirstName("John")
                .setLastName("ThisIsAReallyLongLastNameThatExceedsTheMaximumAllowedLength")
                .setPassword("StrongPassword123")
                .setAdmin(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("la taille doit être comprise entre 0 et 20");
    }

    @Test
    public void whenFirstNameExceedsMaxLength_thenReturnConstraintViolation() {
        User user = new User()
                .setEmail("john.doe@example.com")
                .setFirstName("ThisIsAReallyLongFirstNameThatExceedsTheMaximumAllowedLength")
                .setLastName("Doe")
                .setPassword("StrongPassword123")
                .setAdmin(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("la taille doit être comprise entre 0 et 20");
    }

}
