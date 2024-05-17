package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void whenLoadUserByUsername_WhenUserExists_ShouldFindTheCorrectUserByEmail() {
        String email = "user@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());

        verify(userRepository).findByEmail(email);
    }

    @Test
    void whenLoadUserByUsername_WhenUserNotFound_ShouldThrowException() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(email));

        assertTrue(exception.getMessage().contains("User Not Found with email: " + email));
    }
}
