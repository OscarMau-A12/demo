package com.example.demo.dtos;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NewProjectDto {

    @NotEmpty
    private String projectName;

    private String description;

    @NotNull
    @NotEmpty
    private List<String> collaborators;

}
