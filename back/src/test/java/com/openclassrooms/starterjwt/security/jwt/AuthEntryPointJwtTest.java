package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthEntryPointJwtTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private AuthenticationException authException;

    private AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    AuthEntryPointJwtTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCommence_thenSetUnauthorizedResponse() throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(request.getServletPath()).thenReturn("/test/path");
        when(authException.getMessage()).thenReturn("Unauthorized error");

        authEntryPointJwt.commence(request, response, authException);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        // You might want to parse the JSON response to further assert the response body details
    }
}
