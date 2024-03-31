package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {


    @InjectMocks
    UserController controllerUnderTest;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private UserDetails mockUserDetails;

    @Mock
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    private ObjectMapper objectMapper;



    private User user;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();

        user = User.builder().firstName("John").lastName("Doe").admin(false).email("john.doe@test.com").password("test!1234").build();
        userDto = UserDto.builder().firstName("John").lastName("Doe").admin(false).email("john.doe@test.com").build();
    }

    @Test
    void whenFindById_IfUserExists_ThenReturnsUser() throws Exception {

        when(userService.findById(anyLong())).thenReturn(user);
        when(userMapper.toDto(Mockito.any(User.class))).thenReturn(userDto);

        ResultActions result = mockMvc.perform(get("/api/user/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(userDto.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(userDto.getLastName())));


        verify(userService, times(1)).findById(anyLong());
        verify(userMapper, times(1)).toDto(Mockito.any(User.class));
    }

}
