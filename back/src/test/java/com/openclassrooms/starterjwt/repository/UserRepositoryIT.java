package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenFindByEmail_thenReturnUser() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password"); // Assuming User entity has these fields
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        entityManager.persistAndFlush(newUser);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void whenFindByEmail_thenReturnEmpty() {
        // Given no user is saved

        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertThat(foundUser).isNotPresent();
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        User newUser = new User();
        newUser.setEmail("exists@example.com");
        newUser.setPassword("password");
        entityManager.persistAndFlush(newUser);

        Boolean exists = userRepository.existsByEmail("exists@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    public void whenNotExistsByEmail_thenReturnFalse() {
        // Given no user is saved

        // When
        Boolean exists = userRepository.existsByEmail("doesnotexist@example.com");

        // Then
        assertThat(exists).isFalse();
    }
}
