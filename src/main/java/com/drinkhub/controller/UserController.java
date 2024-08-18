package com.drinkhub.controller;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.entity.User;
import com.drinkhub.service.UserService;
import com.drinkhub.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) throws Exception {
        return userService.login(loginDto);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // Encode the user's password before saving
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.save(user);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }
}
