package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ContextConfiguration(classes = SpringBootSecurityJwtApplication.class)
public class TeacherRepositoryIT {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenSaveNewTeacher_thenSuccess() {
        // Given
        Teacher newTeacher = new Teacher();
        newTeacher.setFirstName("Jane");
        newTeacher.setLastName("Doe");
        // Optionally set other properties of the Teacher

        // When
        Teacher savedTeacher = teacherRepository.save(newTeacher);

        // Then
        Teacher foundTeacher = entityManager.find(Teacher.class, savedTeacher.getId());
        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getFirstName()).isEqualTo(newTeacher.getFirstName());
        assertThat(foundTeacher.getLastName()).isEqualTo(newTeacher.getLastName());
        // Assert other properties as needed
    }

    @Test
    public void whenFindById_thenReturnTeacher() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        // Optionally set other properties of the Teacher
        entityManager.persistAndFlush(teacher); // Directly using entityManager to persist the teacher for setup

        // When
        Teacher foundTeacher = teacherRepository.findById(teacher.getId()).orElse(null);

        // Then
        assertThat(foundTeacher).isNotNull();
        assertThat(foundTeacher.getId()).isEqualTo(teacher.getId());
        assertThat(foundTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(foundTeacher.getLastName()).isEqualTo(teacher.getLastName());
        // Assert other properties as needed
    }
}
