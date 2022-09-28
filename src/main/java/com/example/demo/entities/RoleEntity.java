package com.example.demo.entities;

import com.example.demo.configurations.RoleList;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleList roleName;

}
