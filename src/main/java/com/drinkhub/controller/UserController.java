package com.drinkhub.controller;

import com.drinkhub.model.entity.User;
import com.drinkhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    // აუტოვაირების შეცვლა ფინალით ლომბოკი
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }
}
