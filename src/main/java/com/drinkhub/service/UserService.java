package com.drinkhub.service;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.User;
import com.drinkhub.repository.UserRepository;
import com.drinkhub.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public String login(LoginDto loginDto) throws Exception {

        User user = userRepository.findByUsername(loginDto.username());

        if (user == null || !passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new Exception("Invalid username or password");
        }

        // Generate JWT token and return it
        return jwtUtil.generateToken(user.getUsername());
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
