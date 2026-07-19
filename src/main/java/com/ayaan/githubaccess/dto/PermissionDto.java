package com.ayaan.githubaccess.dto;

import lombok.Data;

@Data
public class PermissionDto {

    private boolean admin;
    private boolean push;
    private boolean pull;

}