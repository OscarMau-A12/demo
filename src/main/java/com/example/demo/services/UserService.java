package com.example.demo.services;

import com.example.demo.dtos.TypingUserDto;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.cache.spi.support.SimpleTimestamper.next;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private String localUsername;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityRaw = userRepository.findByUserName(username);
        if(userEntityRaw.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }
        UserEntity userEntity = userEntityRaw.get();
        List<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
        return new User(userEntity.getUserName(), userEntity.getPassword(), authorities);
    }

    public UserEntity getLocalUserInformation() throws Exception {
        Optional<UserEntity> localUserOptional = userRepository.findByUserName(this.localUsername);
        if (localUserOptional.isEmpty()) {
            throw new Exception("Unable to load local user");
        }
        return localUserOptional.get();
    }

    public void setLocalUser(String localUser) {
        this.localUsername = localUser;
    }

    public List<String> findUsersWhileTypingInDB(String userName) {
        List<UserEntity> users = userRepository.findByUserNameContainingIgnoreCase(userName);
        List<String> userNames = users.stream()
                .filter(u -> u.getUserName() != userName)
                .map(UserEntity::getUserName)
                .collect(Collectors.toList());
        return userNames;
    }

}
