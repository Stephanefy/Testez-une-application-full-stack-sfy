package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Log4j2
class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;


    @AfterEach
    void tearDown() {
        // Clear the security context after each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenDoFilterInternalWithValidJwt_thenCorrectlyAuthenticate() throws ServletException, IOException {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvZSBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.XrKvi5l6adFCLJDVPlHcZ9WAnSq0RhKnHw3DwBGI_JQ";
        String username = "test_user";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken expectedAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        expectedAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        UsernamePasswordAuthenticationToken actualAuthentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(expectedAuthentication, actualAuthentication);
        verify(jwtUtils, times(1)).validateJwtToken(jwt);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void whenDoFilterInternalWithInvalidJwt_thenDoNotAuthenticate() throws ServletException, IOException {
        // Arrange
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        UsernamePasswordAuthenticationToken actualAuthentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        log.info("actual auth {}", actualAuthentication);
        assertNull(actualAuthentication);

    }

    @Test
    void whenDoFilterInternalWithNoJwt_ThenDoNotAuthenticate() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        UsernamePasswordAuthenticationToken actualAuthentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNull(actualAuthentication);
    }
}