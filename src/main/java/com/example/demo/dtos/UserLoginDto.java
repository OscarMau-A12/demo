package com.example.demo.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginDto {

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

}
