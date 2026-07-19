package com.ayaan.githubaccess.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessReportDto {

    private String username;

    private List<RepositoryAccessDto> repositories;
}