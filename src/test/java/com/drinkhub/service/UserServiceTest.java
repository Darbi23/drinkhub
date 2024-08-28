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

        // Common test data
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
        // Arrange
        UserDto updatedUserDto = new UserDto(1L, "newTestUser", "newTest@example.com", "newTestPass", Role.USER);
        User existingUser = new User("testUser", "test@example.com", "encodedPass", Role.USER);
        User updatedUser = new User("newTestUser", "newTest@example.com", "encodedPass", Role.USER);
        updatedUser.setId(1L);  // Set the same ID to simulate the update

        // Mock the behavior of the repository and mapper
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(updatedUserDto);

        // Act
        UserDto result = userService.updateUserProfile("testUser", updatedUserDto);

        // Assert
        assertNotNull(result);
        assertEquals("newTestUser", result.getUsername());  // This checks that the username was updated correctly
        assertEquals("newTest@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));  // Verify that the save method was called exactly once
    }

    @Test
    void testDeleteUser_Success() {
        // Arrange
        User user = new User("testUser", "test@example.com", "encodedPass", Role.USER);
        user.setId(1L);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser("testUser");

        // Assert
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

        // Ensure the mock returns the user object correctly.
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        // Ensure the password matching mock returns true
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        // Ensure the mock for token generation returns a mock token
        when(jwtUtil.generateToken(username, user.getId(), "ROLE_USER")).thenReturn(expectedToken);

        LoginDto loginDto = new LoginDto(username, password);

        // Act
        String result = userService.login(loginDto);

        // Assert
        assertNotNull(result, "Token should not be null after successful login");
        assertEquals(expectedToken, result, "Expected token should match the result token");
    }


    @Test
    void testLogin_Failure_InvalidPassword() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(passwordEncoder.encode("testPass"));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        LoginDto loginDto = new LoginDto("testUser", "wrongPass"); // Use wrong password for test

        // Act & Assert
        assertThrows(Exception.class, () -> userService.login(loginDto));
    }


    @Test
    void testRegisterUser_Success() {
        // Arrange
        UserDto userDto = new UserDto(null, "testUser", "test@example.com", "testPass", Role.USER);
        User user = new User(userDto.getUsername(), userDto.getEmail(), "encodedPass", userDto.getRole());

        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userMapper.toDto(any(User.class))).thenReturn(new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, user.getRole()));

        // Act
        UserDto result = userService.registerUser(userDto);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_Failure_UserAlreadyExists() {
        // Arrange
        UserDto userDto = new UserDto(null, "testUser", "test@example.com", "testPass", Role.USER);
        User existingUser = new User(userDto.getUsername(), userDto.getEmail(), "encodedPass", userDto.getRole());

        // Mock the behavior: the repository should return an Optional containing the existing user
        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(existingUser));
        // Optional: Also mock for email to handle both username and email existing scenarios
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.registerUser(userDto));

        // Verify that the userRepository's save method was never called
        verify(userRepository, never()).save(any(User.class));
    }


}
