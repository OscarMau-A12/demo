package com.example.demo.controllers;

import com.example.demo.dtos.NewProjectDto;
import com.example.demo.entities.ProjectEntity;
import com.example.demo.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<ProjectEntity> saveProject(@RequestBody NewProjectDto projectDto) throws ResponseStatusException {
        try {
            ProjectEntity project = projectService.createProjectInDB(projectDto);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

    @GetMapping("/getAllByLocalUser")
    public ResponseEntity<List<ProjectEntity>> getAllProjectsByLocalUser()  throws ResponseStatusException {
        try {
            List<ProjectEntity> projectsByUser = projectService.getAllProjectsByUserFromDB();
            return new ResponseEntity<>(projectsByUser, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<List<ProjectEntity>> deleteProject(@PathVariable Long projectId)  throws ResponseStatusException {
        try {
            List<ProjectEntity> projectsByUser = projectService.deleteProjectByIdInDB(projectId);
            return new ResponseEntity<>(projectsByUser, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

}
