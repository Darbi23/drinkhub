package com.drinkhub.model.mapper;

import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(),null, user.getRole());
    }

    public User toEntity(UserDto userDto) {
        return new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), userDto.getRole());
    }

    public void updateEntityFromDto(UserDto userDto, User existingUser) {
        if (userDto.getUsername() != null) {
            existingUser.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword())); // Assuming passwordEncoder is available
        }
        if (userDto.getRole() != null) {
            existingUser.setRole(userDto.getRole());
        }
    }
}
