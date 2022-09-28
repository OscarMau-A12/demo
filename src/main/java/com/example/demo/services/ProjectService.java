package com.example.demo.services;

import com.example.demo.dtos.NewProjectDto;
import com.example.demo.entities.ProjectEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectEntity createProjectInDB(NewProjectDto projectDto) throws Exception {
        ProjectEntity projectToSave = new ProjectEntity();
        projectToSave.setProjectName(projectDto.getProjectName());
        projectToSave.setDescription(projectDto.getDescription());
        projectToSave.setUserEntity(userService.getLocalUserInformation());
        ProjectEntity projectSaved = projectRepository.save(projectToSave);

        List<UserEntity> collaboratorsToSave = userRepository.findByUserNameIn(projectDto.getCollaborators());
        if (collaboratorsToSave.isEmpty()) {
            throw new Exception("Unable to find collaborators");
        }

        List<UserEntity> collaboratorsRaw = collaboratorsToSave
                .stream().map(
                c -> {
                    c.getProjects().add(projectSaved);
                    return c;
                }).collect(Collectors.toList());
        List<UserEntity> collaboratorsSaved = userRepository.saveAll(collaboratorsRaw);
        if (collaboratorsSaved.isEmpty()) {
            throw new Exception("Unable to save assign project to collaborators");
        }

        return projectSaved;
    }

    public List<ProjectEntity> getAllProjectsByUserFromDB() throws Exception {
        UserEntity localUser = userService.getLocalUserInformation();
        List<ProjectEntity> projects = localUser.getProjects();
        if (projects.isEmpty()) {
            throw new Exception("No projects stored yet for user" + localUser.getUserName());
        }
        return projects;
    }

    public List<ProjectEntity> deleteProjectByIdInDB(Long projectId) throws Exception {
        Optional<ProjectEntity> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            throw new Exception("Project not found");
        }
        ProjectEntity projectToDelete = projectOptional.get();
        UserEntity localUser = userService.getLocalUserInformation();
        if (!projectToDelete.getUserEntity().equals(localUser)) {
            throw new Exception("You are not the owner");
        }
        projectRepository.delete(projectToDelete);
        //projectToDelete.setEliminationDate(new Timestamp(new Date().getTime()));
        return  getAllProjectsByUserFromDB();
    }

}
