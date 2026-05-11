package com.ecommerceapp.store.mappers;

import com.ecommerceapp.store.dtos.UserDto;
import com.ecommerceapp.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
}
