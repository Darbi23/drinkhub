package com.drinkhub.service;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.UserMapper;
import com.drinkhub.repository.UserRepository;
import com.drinkhub.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDto registerUser(UserDto userDto) {

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists with this username");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
    public String login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.username())
                .orElseThrow(() -> new RuntimeException("Invalid username or password ( username not found)"));

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password ( incorrect password)");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getId(), String.valueOf(user.getRole()));
    }

    public UserDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }
    @Transactional
    public UserDto updateUserProfile(String username, UserDto userDto) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        userMapper.updateEntityFromDto(userDto, existingUser);
        existingUser = userRepository.save(existingUser);
        return userMapper.toDto(existingUser);
    }
    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        System.out.println("Deleting user with username: " + username);
        System.out.println("User Role: " + user.getRole());
        // Perform any additional operations if needed before deletion
        userRepository.delete(user);
    }
}
