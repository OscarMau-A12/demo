package com.example.demo.controllers;

import com.example.demo.entities.FileEntity;
import com.example.demo.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/layout")
    public String showUploadLocalPage() {
        return "uploadFile";
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestPart("data") MultipartFile file) throws ResponseStatusException {
        try {
            fileService.saveFileToDB(file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

    @GetMapping("/getAllByLocalUser")
    public ResponseEntity<List<FileEntity>> getAllFilesByLocalUser()  throws ResponseStatusException{
         try {
             List<FileEntity> filesByUser = fileService.getAllFilesByUserFromDB();
             return new ResponseEntity<>(filesByUser, HttpStatus.OK);
         } catch (Exception e) {
             throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
         }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<FileEntity> obtainFileByFileName(@PathVariable String fileName) {
        try {
            FileEntity file = fileService.getFileByFileNameFromDB(fileName);
            return new ResponseEntity<>(file, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFileByFileName(@PathVariable String fileName) {
        try {
            fileService.deleteFileByFileNameFromDB(fileName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

}
