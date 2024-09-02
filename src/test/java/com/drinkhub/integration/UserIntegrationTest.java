package com.drinkhub.integration;

import com.drinkhub.model.dto.LoginDto;
import com.drinkhub.model.dto.UserDto;
import com.drinkhub.model.entity.Role;
import com.drinkhub.repository.UserRepository;
import com.drinkhub.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserRegistrationAndLogin() {
        UserDto userDto = new UserDto(null, "testUser2", "test2@example.com", "testPass", Role.USER);
        LoginDto loginDto = new LoginDto("testUser2", "testPass");

        try {
            UserDto registeredUser = userService.registerUser(userDto);
            assertNotNull(registeredUser.getId(), "User ID should be set after registration");

            String token = userService.login(loginDto);
            assertNotNull(token, "Token should not be null after a successful login");

        } finally {
            userRepository.findByUsername("testUser2").ifPresent(userRepository::delete);
        }
    }
}
