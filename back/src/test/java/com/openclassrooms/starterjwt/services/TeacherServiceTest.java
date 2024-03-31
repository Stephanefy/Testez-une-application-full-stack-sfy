package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    // Setup and utility methods...

    @Test
    public void whenFindAll_thenReturnListOfTeachers() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);

        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        List<Teacher> result = teacherService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void whenFindById_thenReturnTeacher() {
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher found = teacherService.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(teacherRepository, times(1)).findById(id);
    }

    @Test
    public void whenFindById_thenNotFound() {
        Long id = 1L;
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        Teacher found = teacherService.findById(id);

        assertNull(found);
        verify(teacherRepository, times(1)).findById(id);
    }


}
