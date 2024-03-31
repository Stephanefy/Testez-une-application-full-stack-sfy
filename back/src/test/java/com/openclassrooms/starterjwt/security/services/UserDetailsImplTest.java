package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDetailsImplTest {

    @Test
    void whenEquals_SameId_ShouldReturnTrue() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("user")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("user")
                .build();

        assertTrue(user1.equals(user2));
    }

    @Test
    void whenEquals_DifferentId_ShouldReturnFalse() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("user")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(2L)
                .username("user")
                .build();

        assertFalse(user1.equals(user2));
    }

    @Test
    void getAuthorities_ShouldReturnEmptySet() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("user")
                .build();

        assertTrue(user.getAuthorities().isEmpty());
    }


}
