package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Log4j2
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
        when(authException.getMessage()).thenReturn("Unauthorized error message");

        authEntryPointJwt.commence(request, response, authException);

        Map<String, Object> expectedBody = new HashMap<>();

        expectedBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        expectedBody.put("error", "Unauthorized");
        expectedBody.put("message", "Unauthorized error message");
        expectedBody.put("path", "/test/path");

        ObjectMapper mapper = new ObjectMapper();

        String expectedResponse = mapper.writeValueAsString(expectedBody);
        String actualResponse = response.getContentAsString();



        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals(expectedResponse, actualResponse);

    }
}
