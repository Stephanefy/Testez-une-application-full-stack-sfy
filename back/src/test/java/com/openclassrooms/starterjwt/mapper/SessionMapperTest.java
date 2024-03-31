package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

//    @BeforeEach
//    void setUp() {
//        // Set up mocks for teacherService and userService
//        // Example
//        when(teacherService.findById(anyLong())).thenReturn(new Teacher(/* set up teacher */));
//        when(userService.findById(anyLong())).thenReturn(new User(/* set up user */));
//    }

    @Test
    void whenConvertSessionDtoToSession_thenCorrect() {
        // Define a SessionDto with expected values
        SessionDto sessionDto = SessionDto.builder().name("Learn Lotus pose").description("spend a whole day mastering the Lotus pose").teacher_id(1L).build();

        // Call toEntity
        Session session = sessionMapper.toEntity(sessionDto);

        // Assert the Session's fields are correctly populated
        assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());

    }

    @Test
    void whenConvertSessionToSessionDto_thenCorrect() {
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
}
