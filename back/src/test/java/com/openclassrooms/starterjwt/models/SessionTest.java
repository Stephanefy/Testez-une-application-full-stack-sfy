package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@Log4j2
public class SessionTest {


    @Mock
    private Validator validator;

    @Mock
    private SessionRepository repository;

    @InjectMocks
    private SessionService service;

    private Session session;

    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        session = new Session()
                .setId(1L)
                .setName("Test Session")
                .setDate(new Date())
                .setDescription("This is a test description")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        Teacher teacher = new Teacher(); // Assume Teacher is another entity similar to Session
        session.setTeacher(teacher);

        User user1 = new User(); // Assume User is another entity
        User user2 = new User();
        session.setUsers(Arrays.asList(user1, user2));
    }



    @Test
    public void whenSessionIsValid_thenReturnNoConstraintViolations() {
        Session session = new Session()
                .setName("Yoga Class")
                .setDate(new Date())
                .setDescription("A relaxing yoga session");

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertThat(violations.isEmpty());
    }


    @Test
    public void whenSessionNameIsBlank_thenReturnConstraintViolation() {
        Session session = new Session()
                .setName("")
                .setDate(new Date())
                .setDescription("A relaxing yoga session");

        ConstraintViolation<Session> violation = mock(ConstraintViolation.class);

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        violations.add(violation);

        assertThat(violations.toString().contains("ne doit pas être vide'"));


    }

    @Test
    public void whenSessionNameIsOverMaxLimit_thenReturnConstraintViolation() {
        Session session = new Session()
                .setName("Enlightened Journey Wellness Center: A Haven for Mind, Body, and Soul Rejuvenation")
                .setDate(new Date())
                .setDescription("A relaxing yoga session");

        ConstraintViolation<Session> violation = mock(ConstraintViolation.class);

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        violations.add(violation);

        log.info(violations);

        assertThat(violations.toString().contains("la taille doit être comprise entre 0 et 50"));
    }

}
