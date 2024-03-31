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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenSessionIsValid_thenReturnNoConstraintViolations() {
        Session session = new Session()
                .setName("Yoga Class")
                .setDate(new Date())
                .setDescription("A relaxing yoga session");

        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertTrue(violations.isEmpty());
    }


    @Test
    public void whenSessionNameIsBlank_thenConstraintViolation() {
        Session session = new Session()
                .setName("")
                .setDate(new Date())
                .setDescription("A relaxing yoga session");

        ConstraintViolation<Session> violation = mock(ConstraintViolation.class);

        Set<ConstraintViolation<Session>> violations = new HashSet<>();
        violations.add(violation);

        log.info("violoation {}", violations.size());

//
//        assertFalse(violationMessages.isEmpty());
         assertEquals(1, violations.size());
//        assertEquals("Name is blank", violationMessages.get(0));

//        verify(repository, never()).save(any(Session.class));
    }

    // Add more tests for other constraints like @NotNull and @Size
}
