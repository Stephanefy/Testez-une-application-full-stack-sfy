package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // Setup and utility methods...

    @Test
    public void whenDelete_thenRepositoryDeletes() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void whenFindById_thenReturnUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User found = userService.findById(userId);

        assertNotNull(found);
        assertEquals(userId, found.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void whenFindById_thenNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User found = userService.findById(userId);

        assertNull(found);
        verify(userRepository, times(1)).findById(userId);
    }


}
