package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Integration tests for {@link SessionController} */
@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @WithMockUser(username="user",roles={"USER"})
    public void testListSessions_ReturnsCorrectNumberOfElementsRecorded() throws Exception {

        mockMvc
                .perform(get("/api/session"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username="user",roles={"USER"})
    public void testFindSessionById_ShouldReturnTheCorrectSession() throws Exception {
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        // Add more assertions as necessary based on your SessionDto structure
    }
    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void testPostAnewSession_ShouldReturnSuccessAndTheSession() throws Exception {
        SessionDto newSessionDto = SessionDto.builder()
                .name("Session 3")
                .description("Description of session 3")
                .date(new Date())
                .teacher_id(1L)
                .build();


        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newSessionDto))
                )
                .andDo(print())
                .andExpect(status().isOk());
        // make another request to check if DB is returning +1 session element
        mockMvc
                .perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

    }


    @Test
    @WithMockUser(username="user", roles={"USER"})
    public void findSessionById_NonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/session/9999") // Assuming ID 9999 does not exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
}