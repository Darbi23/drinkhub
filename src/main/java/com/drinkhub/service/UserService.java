package com.drinkhub.service;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.UserMapper;
import com.drinkhub.repository.UserRepository;
import com.drinkhub.utils.JwtUtil;
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

//    public UserDto registerUser(UserDto userDto) {
//        User user = userMapper.toEntity(userDto);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user = userRepository.save(user);
//        return userMapper.toDto(user);
//    }

    public UserDto registerUser(UserDto userDto) {
        // Log the received userDto
        System.out.println("Registering User with username: " + userDto.getUsername() + ", email: " + userDto.getEmail());

        // Hash the password
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());

        // Set the hashed password
        userDto.setPassword(hashedPassword);

        // Convert DTO to Entity and save user
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);

        // Return the saved user's DTO
        return userMapper.toDto(user);
    }
    public String login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        // Generate and return the JWT token
        return jwtUtil.generateToken(user.getUsername(), user.getId());
    }

    public UserDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    public UserDto updateUserProfile(String username, UserDto userDto) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        userMapper.updateEntityFromDto(userDto, existingUser);
        existingUser = userRepository.save(existingUser);
        return userMapper.toDto(existingUser);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
