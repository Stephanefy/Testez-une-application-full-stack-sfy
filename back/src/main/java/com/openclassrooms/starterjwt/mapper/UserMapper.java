package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;

@Component
@Mapper(componentModel = "spring")
@Generated("org.mapstruct")
public interface UserMapper extends EntityMapper<UserDto, User> {
}
