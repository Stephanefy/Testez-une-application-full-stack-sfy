package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;


    @Test
    void whenConvertSessionDtoToSessionEntity_thenShouldCorrectlyMapToSessionEntity() {
        // Define a SessionDto with expected values
        SessionDto sessionDto = SessionDto.builder().name("Learn Lotus pose").description("spend a whole day mastering the Lotus pose").teacher_id(1L).build();

        // Call toEntity
        Session session = sessionMapper.toEntity(sessionDto);

        // Assert the Session's fields are correctly populated
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());

    }

    @Test
    void whenConvertSessionToSessionDto_thenShouldCorrectlyMapToSessionDto() {
        // Define a Session with expected values
        Teacher teacher = Teacher.builder().firstName("John").lastName("Doe").id(1L).build();

        Session session = Session.builder()
                .name("Learn the Warrior stance")
                .date(new Date())
                .teacher(teacher)
                .description("A full and comprehensive session about learning to land your first Warrior stance")
                .build();

        // Call toDto
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Assert the SessionDto's fields are correctly populated
        assertThat(sessionDto.getDescription()).isEqualTo(session.getDescription());

    }

    @Test
    void whenSessionDTOHasNoUsersOrTeacher_thenShouldMapToNull() {
        SessionDto sessionDto = SessionDto.builder().name("Learn Lotus pose").description("spend a whole day mastering the Lotus pose").build();

        Session session = sessionMapper.toEntity(sessionDto);

        assertThat(session.getUsers()).isEmpty();
        assertThat(session.getTeacher()).isNull();

    }


    @Test
    public void testToEntityCompleteMapping() {
        // Create a sample SessionDto
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(1L);  // Assuming the teacher with ID 1 exists
        sessionDto.setUsers(Arrays.asList(2L, 3L)); // Assuming these user IDs exist

        // Create corresponding Teacher and User entities that the service should return
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);

        // Setup service mocks
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(2L)).thenReturn(user1);
        when(userService.findById(3L)).thenReturn(user2);

        // Call the method under test
        Session session = sessionMapper.toEntity(sessionDto);

        // Validate the results
        assertEquals("Test Session", session.getDescription(), "Description should match");
        assertEquals(teacher, session.getTeacher(), "Teacher should match");
        assertEquals(Arrays.asList(user1, user2), session.getUsers(), "User list should match the provided IDs");

        // Verify interactions with the mock objects
        verify(teacherService).findById(1L);
        verify(userService).findById(2L);
        verify(userService).findById(3L);
    }

}
