package com.example.demo.controllers;

import com.example.demo.dtos.TypingUserDto;
import com.example.demo.entities.UserEntity;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/local")
    public ResponseEntity<UserEntity> getLocalUser() throws ResponseStatusException {
        try {
            UserEntity localUser = userService.getLocalUserInformation();
            return new ResponseEntity<>(localUser, HttpStatus.OK);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

    @GetMapping("/{userName}")
    public ResponseEntity<List<String>> getUserWhileTyping(@PathVariable String userName) throws ResponseStatusException {
        try {
            List<String> usersNames = userService.findUsersWhileTypingInDB(userName);
            return new ResponseEntity<>(usersNames, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

}
