package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Teacher teacher;

    private User user;

    private List<User> particpatingUsers = new ArrayList<>();

    private Long sessionId = 1L;


    @BeforeEach
    public void setup(){
         user = User.builder().id(1L).email("john@test.com").firstName("John").lastName("Doe").admin(false).password("password").build(); // Mock user setup
         teacher = Teacher.builder().id(1L).firstName("Alice").firstName("Murphy").build();

    }


    @Test
    public void whenFindAll_thenReturnsListOfSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session()); // Add mock sessions as needed
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> foundSessions = sessionService.findAll();

        assertNotNull(foundSessions);
        assertFalse(foundSessions.isEmpty());
        assertEquals(sessions.size(), foundSessions.size());
        verify(sessionRepository).findAll();
    }

    @Test
    public void whenParticipateWithValidIds_thenSuccess() {

        Session session = Session.builder().id(1L).description("Learn Warrior stance").users(particpatingUsers).teacher(teacher).build(); // Mock session setup
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Assuming session.getUsers() is properly initialized in the mock Session object
        sessionService.participate(sessionId, user.getId());

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    public void whenUserAlreadyParticipateToASession_ThenThrowBadRequest() {
        particpatingUsers.add(user);
        Session session = Session.builder().id(1L).description("Learn Warrior stance").users(particpatingUsers).teacher(teacher).build(); // Mock session setup

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));


        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(sessionId, user.getId());
        });

    }

    @Test
    public void whenUserNoLongerParticipate_thenReturnSuccessWithSessionWithoutUserId(){
        particpatingUsers.add(user);
        Session session = Session.builder().id(1L).description("Learn Warrior stance").users(particpatingUsers).teacher(teacher).build();

        session.setUsers(session.getUsers().stream().filter(user -> !user.getId().equals(user.getId())).collect(Collectors.toList()));

        assertFalse(session.getUsers().contains(user));

    }

    @Test
    public void whenParticipateAndSessionNotFound_thenThrowNotFoundException() {

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, user.getId());
        });
    }



}
