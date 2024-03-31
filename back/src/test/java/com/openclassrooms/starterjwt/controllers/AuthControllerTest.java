package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController controllerUnderTest;
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private JwtResponse mockJwtResponse;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    private LoginRequest mockLoginRequest;

    private SignupRequest mockSignUpRequest;

    private String mockJwt;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();


        mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();


        // Mock authentication process
        Authentication mockAuth = org.mockito.Mockito.mock(Authentication.class);

        mockLoginRequest = LoginRequest.builder().email("user@example.com").password("password").build();
        mockSignUpRequest = SignupRequest.builder().firstName("John").lastName("Doe").email("user@example.com").password("password").build();
        mockJwt = "mockJwtToken";
    }


    @Test
    public void whenAuthenticateUser_thenReturnJwtResponse() throws Exception {

        // Mock the authentication process
        mockLoginRequest = LoginRequest.builder().email("user@example.com").password("password").build();
//        mockSignUpRequest = SignupRequest.builder().firstName("John").lastName("Doe").email("user@example.com").password("password").build();
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetailsImpl mockUserDetails = new UserDetailsImpl(
                1L,
                "user@example.com",
                "John",
                "Doe",
                false,
               Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")).toString()
        );
//        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user@example.com", "John", "Doe", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")).toString());
        User user = new User(); // Assuming there's a constructor or setters to set user details
        user.setAdmin(true);
        String mockJwt = "mockJwtToken";

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(mockJwt);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Perform POST request and expect JwtResponse
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(mockJwt))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockUserDetails.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(mockUserDetails.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(mockUserDetails.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(mockUserDetails.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.admin").value(user.isAdmin()));

        // Verification steps, if necessary
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtils, times(1)).generateJwtToken(any(Authentication.class));
        verify(userRepository, times(1)).findByEmail(anyString());
    }


    @Test
    public void whenRegisterUser_thenReturnSuccessMessage() throws Exception {

        // Simulate the behavior of userRepository and passwordEncoder
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("EncodedPassword");

        // Perform the POST request and assert the response
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockSignUpRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully!"));

        // Verify interactions with mocks
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }


}
