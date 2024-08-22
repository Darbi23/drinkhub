package com.drinkhub.controller;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.User;
import com.drinkhub.service.UserService;
import com.drinkhub.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
            String token = userService.login(loginDto);
            return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        try {
            // Log the incoming userDto to check if it contains the expected data
            System.out.println("Received UserDto for registration: " + userDto);
            System.out.println("Received UserDto for registration: " + userDto.getUsername() + "  " + userDto.getEmail());

            UserDto registeredUser = userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(null);
        } catch (Exception ex) {
            ex.printStackTrace();  // Log the exception stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/{username}")
    public UserDto getUserProfile(@PathVariable String username) {
        return userService.getUserProfile(username);
    }

    @PutMapping("/{username}")
    public UserDto updateUserProfile(@PathVariable String username, @RequestBody UserDto userDto) {
        return userService.updateUserProfile(username, userDto);
    }


    //TODO არ მუშაობს წაშლა
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }
}
