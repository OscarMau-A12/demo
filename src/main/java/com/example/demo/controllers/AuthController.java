package com.example.demo.controllers;

import com.example.demo.configurations.RoleList;
import com.example.demo.dtos.NewUserDto;
import com.example.demo.dtos.UserLoginDto;
import com.example.demo.entities.RoleEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin", origins = "http://localhost:3000/**")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto userLoginDto, BindingResult bidBindingResult){
        if(bidBindingResult.hasErrors()) {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userLoginDto.getUserName(), userLoginDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>("Check your credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin", origins = "http://localhost:3000/**")
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody NewUserDto newUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(newUserDto.getUserName());
        userEntity.setEmail(newUserDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(newUserDto.getPassword()));

        Optional<RoleEntity> roleRawUser = roleRepository.findByRoleName(RoleList.ROLE_USER);
        if (roleRawUser.isEmpty()) {
           return null;
        }
        RoleEntity roleUser = roleRawUser.get();
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleUser);

        if (newUserDto.getRoles().contains("ROLE_ADMIN")) {
            Optional<RoleEntity> roleRawAdmin = roleRepository.findByRoleName(RoleList.ROLE_ADMIN);
            if (roleRawAdmin.isEmpty()) {
                return null;
            }
            RoleEntity roleAdmin = roleRawAdmin.get();
            roles.add(roleAdmin);
        }
        userEntity.setRoles(roles);

        userRepository.save(userEntity);
        return new ResponseEntity<>("Registration successfully", HttpStatus.OK);
    }

}
