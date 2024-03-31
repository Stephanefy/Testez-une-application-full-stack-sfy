package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Log4j2
public class SessionControllerTest {

    @InjectMocks
    SessionController controllerUnderTest;

    private MockMvc mockMvc;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    private ObjectMapper objectMapper;

    private Session session;

    private SessionDto sessionDto;


    @BeforeEach
    public void init() {

        objectMapper = new ObjectMapper();


        mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();


        session = Session.builder()
                .id(1L)
                .name("Warrior stance")
                .description("Learn the warrior stance")
                .date(new Date())
                .build();
        sessionDto = SessionDto.builder()
                .name("Warrior stance")
                .teacher_id(1L)
                .description("Learn the warrior stance")
                .date(new Date())
                .build();
    }

    @Test
    public void whenRequestingAllSession_ReturnSessionsDtoList_of_5() throws Exception {
        List<Session> sessions = new ArrayList<>();
        List<SessionDto> sessionsDto = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            sessionsDto.add(this.sessionDto);
            sessions.add(this.session);
        }

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(Mockito.<List<Session>>any())).thenReturn(sessionsDto);

        mockMvc.perform(get("/api/session/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(5));

        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(Mockito.<List<Session>>any());
        ;
    }


        @Test
        public void whenRequestToFindSessionById_ReturnSessionDto() throws Exception {
            Long sessionId = 1L;

            when(sessionService.getById(sessionId)).thenReturn(session);
            when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

            mockMvc.perform(get("/api/session/" + sessionId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(sessionDto.getName()))
                    .andExpect(jsonPath("$.description").value(sessionDto.getDescription()));
        }



    @Test
    public void WhenRequestfindByIdThatDoesNotExists_ReturnNotFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isNotFound());

    }


    @Test
    public void WhenFindAllButThereIsNoItems_ReturnAnEmptyList() throws Exception {
        when(sessionService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void WhenCreateSession_ReturnedCreatedDto() throws Exception {

        when(sessionService.create(sessionMapper.toEntity(Mockito.any(SessionDto.class)))).thenReturn(session);
        when(sessionMapper.toDto(Mockito.any(Session.class))).thenReturn(sessionDto);


        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(sessionDto.getName())))
                .andExpect(jsonPath("$.description", CoreMatchers.is(sessionDto.getDescription())));

        verify(sessionService, times(1)).create((sessionMapper.toEntity(Mockito.any(SessionDto.class))));
        verify(sessionMapper, times(1)).toDto(Mockito.any(Session.class));
    }

    @Test
    public void whenUpdateSession_thenReturnUpdatedSessionDto() throws Exception {
        String id = "1";

        when(sessionService.update(Mockito.eq(Long.parseLong(id)), Mockito.any(Session.class))).thenReturn(session);
        when(sessionMapper.toEntity(Mockito.any(SessionDto.class))).thenReturn(session);
        when(sessionMapper.toDto(Mockito.any(Session.class))).thenReturn(sessionDto);

        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Learn the warrior stance")); // adjust this to match an actual property

        verify(sessionService, times(1)).update(eq(Long.parseLong(id)), any(Session.class));
        verify(sessionMapper, times(1)).toEntity(any(SessionDto.class));
        verify(sessionMapper, times(1)).toDto(any(Session.class));
    }

    @Test
    public void WhenDeleteASessionThatIsNotFound_SessionNotFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteASession_ReturnOk() throws Exception {
        Session session = new Session(); // Mock your session
        when(sessionService.getById(anyLong())).thenReturn(session);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());
    }


    @Test
    public void whenParticipateWithValidIdAndUserId_thenRespondWithOk() throws Exception {

        String sessionId = "1";
        String userId = "2";

        doNothing().when(sessionService).participate(Long.parseLong(sessionId), Long.parseLong(userId));


        mockMvc.perform(post("/api/session/" + sessionId + "/participate/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void whenParticipateWithInvalidId_thenRespondWithBadRequest() throws Exception {
        String invalidSessionId = "abc"; // Invalid ID that cannot be parsed to Long
        String userId = "2";


        mockMvc.perform(post("/api/session/" + invalidSessionId + "/participate/" + userId) // Replace "/your-endpoint/" with the actual path
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}
