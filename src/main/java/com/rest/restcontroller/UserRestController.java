package com.rest.restcontroller;

import com.rest.model.User;
import com.rest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private UserService userService;
    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody User user) {
        if(userService.findByName(user.getName())!=null || userService.findByEmail(user.getEmail())!=null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        userService.registerUser(user);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
