package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TeacherMapperTest {

    @InjectMocks
    private TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    void whenMapTeacherToDto_thenCorrect() {
        Teacher teacher = Teacher.builder().id(1l).firstName("John").lastName("Doe").build();

        // Set other fields as necessary

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
        assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
        // Assert other fields as necessary
    }

    @Test
    void whenMapDtoToTeacher_thenCorrect() {
        TeacherDto teacherDto = TeacherDto.builder().id(1l).firstName("John").lastName("Doe").build();

        // Set other fields as necessary

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
        // Assert other fields as necessary
    }
}
