package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    public void findUserBy_ExistingId_ReturnsUser() throws Exception {
        // Assuming there's a user with ID 1 in the database for this test to pass
        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.email").value("yoga@studio.com")
                );
        // Add more assertions as necessary based on your UserDto structure
    }

    @Test
    @WithMockUser(username="user@example.com", roles={"USER"})
    public void deleteUser_WithMatchingAuthenticatedUser_ReturnsOk() throws Exception {

        String userIdToDelete = "2"; // This should match the setup user's ID



        //Assert user exists and that deletion happened
        mockMvc.perform(get("/api/user/" + userIdToDelete))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));

        mockMvc.perform(delete("/api/user/" + userIdToDelete))
                .andDo(print())
                .andExpect(status().isOk());

    }

}
