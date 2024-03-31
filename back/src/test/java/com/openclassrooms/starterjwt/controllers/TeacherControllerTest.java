package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
@Log4j2
public class TeacherControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    TeacherController controllerUnderTest;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;



    @BeforeEach
    void setup(){

        mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();

        teacher = teacher.builder().firstName("John").lastName("Doe").build();
        teacherDto = teacherDto.builder().firstName("John").lastName("Doe").build();
    }


    @Test
    public void whenFindById_thenReturnTeacherDto() throws Exception {
        Long teacherId = 1L;

        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get("/api/teacher/" + teacherId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }


    @Test
    public void whenFindById_ThatDoesNotExists_ThenReturnTeacherNotFound() throws Exception {
        Long teacherId = 2L;

        when(teacherService.findById(teacherId)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/" + teacherId))
                .andExpect(status().isNotFound());
    }


    @Test
    public void whenFindById_InvalidIdFormat() throws Exception {
        String invalidId = "abc";

        mockMvc.perform(get("/api/teacher/" + invalidId))
                .andExpect(status().isBadRequest());
    }





    @Test
    public void whenFindAll_thenReturnTeacherDtoList() throws Exception {
        List<Teacher> teachers = List.of(new Teacher().setFirstName("John").setLastName("Doe"),
                new Teacher().setFirstName("Jane").setLastName("Doe"));

        List<TeacherDto> teacherDtos = IntStream.range(0, teachers.size())
                .mapToObj(index -> {
                    Teacher teacher = teachers.get(index); // Get the teacher at this index
                    TeacherDto dto = new TeacherDto();

                    dto.setId(Long.valueOf(index + 1));

                    return dto;
                })
                .collect(Collectors.toList());

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(anyList())).thenReturn(teacherDtos);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }





}
