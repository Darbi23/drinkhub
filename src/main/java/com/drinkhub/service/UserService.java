package com.drinkhub.service;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.UserMapper;
import com.drinkhub.repository.UserRepository;
import com.drinkhub.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDto registerUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
    public String login(LoginDto loginDto) {
        Optional<User> user = userRepository.findByUsername(loginDto.username());
        if (user == null || !passwordEncoder.matches(loginDto.password(), user.get().getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return jwtUtil.generateToken(user.get().getPassword());
    }

//    public JwtResponseDto loginUser(LoginDto loginDto) {
//        User user = userRepository.findByUsername(loginDto.username())
//                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));
//        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//        String token = jwtUtil.generateToken(user.getUsername());
//        return new JwtResponseDto(token);
//    }

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
