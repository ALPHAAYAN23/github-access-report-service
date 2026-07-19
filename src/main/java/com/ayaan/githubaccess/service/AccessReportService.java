package com.ayaan.githubaccess.service;

import com.ayaan.githubaccess.client.GitHubClient;
import com.ayaan.githubaccess.dto.CollaboratorDto;
import com.ayaan.githubaccess.dto.RepositoryAccessDto;
import com.ayaan.githubaccess.dto.RepositoryDto;
import com.ayaan.githubaccess.dto.UserAccessReportDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AccessReportService {

    private final GitHubClient gitHubClient;

    public AccessReportService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<UserAccessReportDto> generateAccessReport() {

        // Fetch all repositories
        List<RepositoryDto> repositories = gitHubClient.fetchRepositories();

        // username -> repositories mapping
        Map<String, List<RepositoryAccessDto>> userRepositoryMap = new HashMap<>();

        // Fetch collaborators asynchronously
        List<CompletableFuture<Void>> futures = repositories.stream()
                .map(repository ->
                        gitHubClient.fetchCollaboratorsAsync(repository.getName())
                                .thenAccept(collaborators -> {

                                    synchronized (userRepositoryMap) {

                                        for (CollaboratorDto collaborator : collaborators) {

                                            String username = collaborator.getLogin();

                                            RepositoryAccessDto repositoryAccess =
                                                    new RepositoryAccessDto();

                                            repositoryAccess.setName(repository.getName());

                                            if (collaborator.getPermissions().isAdmin()) {
                                                repositoryAccess.setPermission("admin");
                                            } else if (collaborator.getPermissions().isPush()) {
                                                repositoryAccess.setPermission("write");
                                            } else {
                                                repositoryAccess.setPermission("read");
                                            }

                                            userRepositoryMap
                                                    .computeIfAbsent(username, k -> new ArrayList<>())
                                                    .add(repositoryAccess);
                                        }
                                    }
                                })
                )
                .collect(Collectors.toList());

        // Wait for all async tasks
        CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).join();

        // Prepare final report
        List<UserAccessReportDto> report = new ArrayList<>();

        for (Map.Entry<String, List<RepositoryAccessDto>> entry : userRepositoryMap.entrySet()) {

            UserAccessReportDto dto = new UserAccessReportDto();
            dto.setUsername(entry.getKey());
            dto.setRepositories(entry.getValue());

            report.add(dto);
        }

        return report;
    }
}