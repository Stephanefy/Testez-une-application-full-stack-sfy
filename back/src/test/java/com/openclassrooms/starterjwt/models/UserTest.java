package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenUserHasValidProperties_thenNoConstraintViolations() {
        User user = new User()
                .setEmail("user@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("StrongPassword123")
                .setAdmin(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Expected no constraint violations");
    }

    @Test
    public void whenEmailIsInvalid_thenConstraintViolation() {
        User user = new User()
                .setEmail("invalidemail")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("StrongPassword123")
                .setAdmin(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Expected constraint violations due to invalid email");
        assertEquals(1, violations.size());
    }

    @Test
    public void whenPasswordExceedsMaxLength_thenConstraintViolation() {
        String longPassword = "ThisPasswordIsWayTooLongAndExceedsTheMaximumAllowedSizejfezkljfklsdflkjdsklfjdsklfdjsklfjqskldfhgqezhithzeuihguiehfuihksdhkgjfhdjgkhsjfkdhjfdkhgfkdjhgjhfdjkhgfkdskk";
        User user = new User()
                .setEmail("user@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword(longPassword)
                .setAdmin(true);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).hasSize(1);
        assertEquals(1, violations.size());
    }


    @Test
    public void testToString() {
        User user = User.builder()
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();
        assertNotNull(user.toString());
    }

    @Test
    public void testGettersAndSetters() {
        User user = new User()
                .setEmail("user@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password")
                .setAdmin(true);

        assertEquals("user@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @Test
    public void testAuditingFields() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User()
                .setCreatedAt(now)
                .setUpdatedAt(now);

        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    public void testEquals() {
        User user1 = new User().setId(1L).setEmail("user@example.com");
        User user2 = new User().setId(1L).setEmail("user@example.com");
        User user3 = new User().setId(2L).setEmail("another@example.com");

        assertEquals(user1, user2); // Should be equal
        assertNotEquals(user1, user3); // Should not be equal
    }

    @Test
    public void testHashCode() {
        User user1 = new User().setId(1L).setEmail("user@example.com");
        User user2 = new User().setId(1L).setEmail("user@example.com");

        assertEquals(user1.hashCode(), user2.hashCode()); // Consistent hashCode for equal objects
    }



}
