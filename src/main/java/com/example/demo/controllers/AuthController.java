package com.example.demo.controllers;

import com.example.demo.configurations.StoreException;
import com.example.demo.dtos.NewUserDto;
import com.example.demo.dtos.ResetUserDto;
import com.example.demo.dtos.UserLoginDto;
import com.example.demo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody UserLoginDto userLoginDto,
            BindingResult bidBindingResult
    ) {
        if (bidBindingResult.hasErrors()) {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        try {
            String jwt = authService.loginInDB(userLoginDto);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody NewUserDto newUserDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        } try {
            authService.registerInDB(newUserDto);
            return new ResponseEntity<>("Registration successfully", HttpStatus.OK);
        } catch (StoreException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetUserDto resetUserDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        } try {
            authService.resetPasswordInDB(resetUserDto);
            return new ResponseEntity<>("Password changed successfully",HttpStatus.OK);
        } catch (StoreException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

}