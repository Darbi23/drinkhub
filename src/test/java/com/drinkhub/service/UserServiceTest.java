package com.drinkhub.service;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.Role;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.UserMapper;
import com.drinkhub.repository.UserRepository;
import com.drinkhub.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User("testUser", "test@example.com", "encodedPass", Role.USER);
        user.setId(1L);

        userDto = new UserDto(1L, "testUser", "test@example.com", null, Role.USER);
    }

    @Test
    void testGetUserByUsername_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserProfile("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testEditUser_Success() {

        UserDto updatedUserDto = new UserDto(1L, "newTestUser", "newTest@example.com", "newTestPass", Role.USER);
        User existingUser = new User("testUser", "test@example.com", "encodedPass", Role.USER);
        User updatedUser = new User("newTestUser", "newTest@example.com", "encodedPass", Role.USER);
        updatedUser.setId(1L);  // Set the same ID to simulate the update

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(updatedUserDto);

        UserDto result = userService.updateUserProfile("testUser", updatedUserDto);

        assertNotNull(result);
        assertEquals("newTestUser", result.getUsername());  // This checks that the username was updated correctly
        assertEquals("newTest@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));  // Verify that the save method was called exactly once
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User("testUser", "test@example.com", "encodedPass", Role.USER);
        user.setId(1L);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        userService.deleteUser("testUser");

        verify(userRepository, times(1)).findByUsername("testUser");
        verify(userRepository, times(1)).delete(user); // Verify delete by entity is called
    }


    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.deleteUser("nonExistentUser"));

        verify(userRepository, times(1)).findByUsername("nonExistentUser");
        verify(userRepository, never()).deleteByUsername(anyString());
    }
    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "testPass";
        String encodedPassword = passwordEncoder.encode(password);
        String expectedToken = "mockToken";  // Assume this is the token that would be generated.

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);
        user.setId(1L);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(username, user.getId(), String.valueOf(user.getRole()))).thenReturn(expectedToken);

        LoginDto loginDto = new LoginDto(username, password);

        String result = userService.login(loginDto);

        assertNotNull(result, "Token should not be null after successful login");
        assertEquals(expectedToken, result, "Expected token should match the result token");
    }


    @Test
    void testLogin_Failure_InvalidPassword() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(passwordEncoder.encode("testPass"));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        LoginDto loginDto = new LoginDto("testUser", "wrongPass"); // Use wrong password for test

        assertThrows(Exception.class, () -> userService.login(loginDto));
    }


    @Test
    void testRegisterUser_Success() {
        UserDto userDto = new UserDto(null, "testUser", "test@example.com", "testPass", Role.USER);
        User user = new User(userDto.getUsername(), userDto.getEmail(), "encodedPass", userDto.getRole());

        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userMapper.toDto(any(User.class))).thenReturn(new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, user.getRole()));

        UserDto result = userService.registerUser(userDto);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_Failure_UserAlreadyExists() {
        UserDto userDto = new UserDto(null, "testUser", "test@example.com", "testPass", Role.USER);
        User existingUser = new User(userDto.getUsername(), userDto.getEmail(), "encodedPass", userDto.getRole());

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.registerUser(userDto));

        verify(userRepository, never()).save(any(User.class));
    }


}
