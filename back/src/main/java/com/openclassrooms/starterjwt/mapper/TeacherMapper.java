package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;

@Component
@Mapper(componentModel = "spring")
@Generated("org.mapstruct")
public interface TeacherMapper extends EntityMapper<TeacherDto, Teacher> {
}
