package com.example.demo.repositories;

import com.example.demo.configurations.RoleList;
import com.example.demo.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {

    Optional<RoleEntity> findByRoleName(RoleList roleList);
    Boolean existsByRoleName(RoleList roleList);

}
