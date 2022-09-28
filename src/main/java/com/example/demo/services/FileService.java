package com.example.demo.services;

import com.example.demo.entities.FileEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.FileRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    public void saveFileToDB(MultipartFile file) throws Exception {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setData(file.getBytes());
        //fileEntity.setUserEntity(userRepository.findByUserName("ossmmoss").get());
        fileEntity.setUserEntity(userService.getLocalUserInformation());
        Optional<FileEntity> fileSaved = Optional.of(fileRepository.save(fileEntity));
        if (fileSaved.isEmpty()) {
            throw new Exception("File not saved");
        }
    }

    public List<FileEntity> getAllFilesByUserFromDB() throws Exception {
        UserEntity localUser= userService.getLocalUserInformation();
        //List<FileEntity> files = fileRepository.findAllByUserEntity(userRepository.findByUserName("ossmmoss").get());
        List<FileEntity> files = fileRepository.findAllByUserEntity(localUser);
        if (files.isEmpty()) {
            throw new Exception("No files stored yet for user" + localUser.getUserName());
        }
        return files;
    }

    public FileEntity getFileByFileNameFromDB(String fileName) throws Exception {
        UserEntity localUser = userService.getLocalUserInformation();
        Optional<FileEntity> fileEntity = fileRepository.findByFileNameAndUserEntity(fileName, localUser);
        if (fileEntity.isEmpty()) {
            throw new Exception("File not found");
        }
        return fileEntity.get();
    }

    public void deleteFileByFileNameFromDB(String fileName) throws Exception {
        UserEntity localUser = userService.getLocalUserInformation();
        Optional<FileEntity> fileEntity = fileRepository.findByFileNameAndUserEntity(fileName, localUser);
        if (fileEntity.isEmpty()) {
            throw new Exception("File not found");
        }
        fileRepository.delete(fileEntity.get());
    }

}