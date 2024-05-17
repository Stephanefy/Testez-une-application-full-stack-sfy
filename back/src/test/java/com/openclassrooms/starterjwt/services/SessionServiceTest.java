package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private Session session;

    private List<User> particpatingUsers = new ArrayList<>();

    private Long sessionId = 1L;


    @BeforeEach
    public void setup(){
         user = User.builder().id(1L).email("john@test.com").firstName("John").lastName("Doe").admin(false).password("password").build();
         teacher = Teacher.builder().id(1L).firstName("Alice").firstName("Murphy").build();
         session = Session.builder().id(1L).description("Learn Warrior stance").users(particpatingUsers).teacher(teacher).build();
    }


    @Test
    public void whenFindAll_thenReturnsListOfSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> foundSessions = sessionService.findAll();

        assertNotNull(foundSessions);
        assertFalse(foundSessions.isEmpty());
        assertEquals(sessions.size(), foundSessions.size());
        verify(sessionRepository).findAll();
    }

    @Test
    public void whenParticipateToASession_ThenReturnSuccess() {



        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        sessionService.participate(session.getId(), user.getId());


        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);

    }



    @Test
    public void whenUserAlreadyParticipateToASession_ThenThrowBadRequest() {

        session.setUsers(Arrays.asList(user));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));



        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(sessionId, user.getId());
        });

    }


    @Test
    public void whenNoLongerParticipateAndSessionNotFound_thenThrowNotFoundException() {
        Long sessionId = 2L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, user.getId());
        });
    }

    @Test
    public void whenUserIsNotParticipatingAndNoLongerParticipate_ThenReturnBadRequest() {
        User newUser = User.builder().id(2L).email("alice@test.com").firstName("Alice").lastName("Grant").admin(false).password("password").build();


        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));


        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, newUser.getId()),
                "Should throw BadRequestException when user is not participating.");
    }

    @Test
    public void whenNoLongerParticipate_ThenReturnSuccessfulRemoval() {

        session.setUsers(Arrays.asList(user));

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(session.getId(), user.getId());

        assertEquals(0, session.getUsers().size(), "User list should have one user after removal.");
        assertFalse(session.getUsers().contains(user), "User list should not contain the removed user.");

        verify(sessionRepository).save(session);

    }



}
