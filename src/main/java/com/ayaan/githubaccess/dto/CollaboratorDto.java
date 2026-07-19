package com.ayaan.githubaccess.dto;

import lombok.Data;

@Data
public class CollaboratorDto {

    private String login;

    private PermissionDto permissions;

}