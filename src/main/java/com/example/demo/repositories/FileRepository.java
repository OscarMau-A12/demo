package com.example.demo.repositories;

import com.example.demo.entities.FileEntity;
import com.example.demo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {

    List<FileEntity> findAllByUserEntity(UserEntity userEntity);
    Optional<FileEntity> findByFileNameAndUserEntity(String fileName, UserEntity user);

}
