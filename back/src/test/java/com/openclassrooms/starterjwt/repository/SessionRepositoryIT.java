package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class SessionRepositoryIT {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void givenNewSession_WhenSave_ThenReturnsSuccess(){

        Teacher teacher = Teacher.builder().firstName("John").lastName("Doe").build();


        Session newSession = Session.builder()
                .name("Learn the Warrior stance")
                .date(new Date())
                .teacher(teacher)
                .description("A full and comprehensive session about learning to land your first Warrior stance")
                .build();
        Session insertedSession = sessionRepository.save(newSession);
        assertThat(entityManager.find(Session.class, insertedSession.getId()) ).isEqualTo(newSession);

    }

    @Test
    public void whenDelete_thenRemoveSession() {
        // Given
        Teacher teacher = entityManager.persistFlushFind(Teacher.builder().firstName("Mike").lastName("Tyson").build());
        Session session = Session.builder()
                .name("Punching Technique")
                .date(new Date())
                .teacher(teacher)
                .description("Learn the basics of a proper punch")
                .build();
        Session persistedSession = entityManager.persistAndFlush(session);

        // When
        sessionRepository.delete(persistedSession);
        Session deletedSession = entityManager.find(Session.class, persistedSession.getId());

        // Then
        assertThat(deletedSession).isNull();
    }

    @Test
    public void whenFindAll_thenReturnAllSessions() {
        // Given
        Teacher teacher1 = entityManager.persistFlushFind(Teacher.builder().firstName("Bruce").lastName("Lee").build());
        Teacher teacher2 = entityManager.persistFlushFind(Teacher.builder().firstName("Chuck").lastName("Norris").build());

        Session session1 = Session.builder()
                .name("Kicking Technique")
                .date(new Date())
                .teacher(teacher1)
                .description("Mastering the art of kicking")
                .build();

        Session session2 = Session.builder()
                .name("Punching Speed")
                .date(new Date())
                .teacher(teacher2)
                .description("Increasing your punching speed")
                .build();

        entityManager.persistAndFlush(session1);
        entityManager.persistAndFlush(session2);

        // When
        List<Session> sessions = sessionRepository.findAll();

        // Assert that the returned list contains 4 elements (initial data + added in this test)
        assertThat(sessions).asList().hasSize(4).contains(session1, session2);
    }


}
