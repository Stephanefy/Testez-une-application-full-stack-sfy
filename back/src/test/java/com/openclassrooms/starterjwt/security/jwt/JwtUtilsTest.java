package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    }

    @Test
    void whenGenerateJwtToken_thenAssertTokenIsGenerated() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test_user");

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void whenGetUserNameFromJwtToken_ThenReturnUsernameFromJwtToken() {
        String token = Jwts.builder()
                .setSubject("test_user")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 3600000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("test_user", username);
    }

    @Test
    void whenValidateCorrectJwtToken_thenReturnTrue() {
        String token = Jwts.builder()
                .setSubject("test_user")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 3600000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void whenValidateJwtTokenWithInvalidSignature_thenReturnFalse() {
        String token = Jwts.builder()
                .setSubject("test_user")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 3600000))
                .signWith(SignatureAlgorithm.HS512, "invalidSecret")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void whenValidateExpiredJwtToken_thenReturnFalse() {
        String token = Jwts.builder()
                .setSubject("test_user")
                .setIssuedAt(new Date(System.currentTimeMillis() - 3600000))
                .setExpiration(new Date(System.currentTimeMillis() - 1800000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }
}
