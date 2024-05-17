package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/** Integration tests for {@link TeacherController} */
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void findAllTeachers_ReturnsCorrectNumberOfElements() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2))); // Adjust the expected size based on setup data
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void findTeacherById_ExistingId_ReturnsTeacher() throws Exception {
        mockMvc.perform(get("/api/teacher/1") // Adjust the ID based on setup data
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Margot"))
                .andExpect(jsonPath("$.lastName").value("DELAHAYE"));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void findTeacherById_NonExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/teacher/9999") // Assuming ID 9999 does not exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
