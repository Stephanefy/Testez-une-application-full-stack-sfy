package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenTeacherHasValidProperties_thenNoConstraintViolations() {
        Teacher teacher = new Teacher()
                .setFirstName("John")
                .setLastName("Doe");

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertTrue(violations.isEmpty(), "Expected no constraint violations");
    }

    @Test
    public void whenTeacherHasBlankLastName_thenConstraintViolation() {
        Teacher teacher = new Teacher()
                .setFirstName("John")
                .setLastName("");

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty(), "Expected constraint violations due to blank last name");
        assertEquals(1, violations.size());
    }

    @Test
    public void whenTeacherLastNameExceedsSizeLimit_thenConstraintViolation() {
        String longName = "ThisNameIsWayTooLongForTheDatabase";
        Teacher teacher = new Teacher()
                .setFirstName("John")
                .setLastName(longName);

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty(), "Expected constraint violations due to last name exceeding size limit");
        assertEquals(1, violations.size());
    }

    // Additional tests can be added for other constraints and properties
}
