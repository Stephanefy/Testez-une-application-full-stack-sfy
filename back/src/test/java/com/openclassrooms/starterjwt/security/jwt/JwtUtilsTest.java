package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // This enables Mockito annotations
class JwtUtilsTest {


    @Test
    void testGenerate_JwtToken() {
        // Setup
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        String jwtSecret = "YourSecretKey";

        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.jwtSecret = jwtSecret; // Ideally injected through @Value
        jwtUtils.jwtExpirationMs = 86400000; // 24 hours
        UserDetailsImpl userDetailsImpl = mock(UserDetailsImpl.class);

        SecurityContextHolder.setContext(securityContext);

        when(userDetailsImpl.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);

        // Execute
        String token = jwtUtils.generateJwtToken(authentication);

        // Verify
        assertNotNull(token);
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        assertEquals("testUser", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertTrue(claims.getExpiration().after(new Date()));

    }

    @Test
    void whenTokenIsValid_thenReturnTrue() {
        JwtUtils jwtUtils = setUpJwtUtils(); // Assume this method sets up JwtUtils with mock values

        String username = "testUser";
        String token = jwtUtils.generateJwtToken(mockAuthentication(username));
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void whenTokenIsExpired_thenReturnFalse() {
        // You might need a way to generate an expired token or mock JwtUtils methods to simulate this scenario.
    }

    private Authentication mockAuthentication(String username) {
        UserDetailsImpl userDetailsImpl = mock(UserDetailsImpl.class);
        when(userDetailsImpl.getUsername()).thenReturn(username);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetailsImpl);
        return authentication;
    }

    private JwtUtils setUpJwtUtils() {
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.jwtSecret = "YourSecretKey";
        jwtUtils.jwtExpirationMs = 60000; // Short expiration time for testing
        return jwtUtils;
    }

}
