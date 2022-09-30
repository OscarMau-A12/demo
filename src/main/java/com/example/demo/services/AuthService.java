package com.example.demo.services;

import com.example.demo.configurations.RoleList;
import com.example.demo.configurations.StoreException;
import com.example.demo.dtos.NewUserDto;
import com.example.demo.dtos.ResetUserDto;
import com.example.demo.dtos.UserLoginDto;
import com.example.demo.entities.RoleEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public String loginInDB(UserLoginDto userLoginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUserName(), userLoginDto.getPassword()
                );
        Authentication authentication =
                authenticationManagerBuilder.getObject()
                        .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        return jwt;
    }

    public void registerInDB(NewUserDto newUserDto) throws StoreException {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(newUserDto.getUserName());
        userEntity.setEmail(newUserDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(newUserDto.getPassword()));

        Boolean existUserName = userRepository.existsByUserName(userEntity.getUserName());
        Boolean existEmail = userRepository.existsByEmail(userEntity.getEmail());
        if (existUserName || existEmail) {
            throw  new StoreException("Information already exist");
        }

        Optional<RoleEntity> roleUserOptional = roleRepository.findByRoleName(RoleList.ROLE_USER);
        if (roleUserOptional.isEmpty()) {
            throw new StoreException("Unable to retrieve roles");
        }
        RoleEntity roleUser = roleUserOptional.get();
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleUser);

        if (newUserDto.getRoles().contains("ROLE_ADMIN")) {
            Optional<RoleEntity> roleAdminOptional = roleRepository.findByRoleName(RoleList.ROLE_ADMIN);
            if (roleAdminOptional.isEmpty()) {
                throw new StoreException("Unable to retrieve roles");
            }
            RoleEntity roleAdmin = roleAdminOptional.get();
            roles.add(roleAdmin);
        }
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
    }

    public void resetPasswordInDB(ResetUserDto resetUserDto) throws StoreException {
        Optional<UserEntity> userOptional = userRepository.findByEmail(resetUserDto.getEmail());
        if (userOptional.isEmpty()) {
            throw new StoreException("Unable to retrieve user");
        }
        UserEntity userRaw = userOptional.get();
        userRaw.setPassword(passwordEncoder.encode(resetUserDto.getResetToken()));
        userRepository.save(userRaw);
    }

}
