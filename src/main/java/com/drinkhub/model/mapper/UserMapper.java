package com.drinkhub.model.mapper;

import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(),null, user.getRole());
    }

    public User toEntity(UserDto userDto) {
        return new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), userDto.getRole());
    }

    public void updateEntityFromDto(UserDto userDto, User existingUser) {
        existingUser.setUsername(userDto.getUsername());
    }
}
