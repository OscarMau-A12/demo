package com.example.demo.services;

import com.example.demo.configurations.RoleList;
import com.example.demo.entities.RoleEntity;
import com.example.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public void createDefaultRoles() {
        Boolean optionalAdmin = roleRepository.existsByRoleName(RoleList.ROLE_ADMIN);
        Boolean optionalUser = roleRepository.existsByRoleName(RoleList.ROLE_USER);
        if (!optionalAdmin && !optionalUser) {
            RoleEntity roleAdmin  = new RoleEntity();
            RoleEntity roleUser  = new RoleEntity();
            roleAdmin.setRoleName(RoleList.ROLE_ADMIN);
            roleUser.setRoleName(RoleList.ROLE_USER);
            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);
        }
    }

}
