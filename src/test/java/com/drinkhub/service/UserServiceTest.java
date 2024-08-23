package com.drinkhub.service;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.Role;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.UserMapper;  // Make sure to import UserMapper
import com.drinkhub.repository.UserRepository;
import com.drinkhub.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize all the @Mock annotated fields
    }

    @Test
    void testLogin_Success() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(passwordEncoder.encode("testPass"));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        when(passwordEncoder.matches(anyString(), anyString())).thenAnswer(invocation -> {
            String rawPassword = invocation.getArgument(0);
            String encodedPassword = invocation.getArgument(1);
            return passwordEncoder.matches(rawPassword, encodedPassword);
        });

        when(jwtUtil.generateToken(anyString(), anyLong(), anyString())).thenReturn("mockToken");

        LoginDto loginDto = new LoginDto("testUser", "testPass");
        String result = userService.login(loginDto);

        // Assert
        assertNotNull(result);
        assertEquals("mockToken", result);

        // Verify interactions
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    void testLogin_Failure_InvalidPassword() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(passwordEncoder.encode("wrongPass"));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        LoginDto loginDto = new LoginDto("testUser", "testPass");

        assertThrows(Exception.class, () -> userService.login(loginDto));
    }

    @Test
    void testRegisterUser_Success() {
        UserDto userDto = new UserDto(null, "testUser", "test@example.com", "testPass", Role.USER);

        User user = new User(userDto.getUsername(), userDto.getEmail(), "encodedPass", userDto.getRole());

        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user); // Mocking UserMapper behavior
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userMapper.toDto(any(User.class))).thenReturn(new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, user.getRole()));

        UserDto result = userService.registerUser(userDto);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
