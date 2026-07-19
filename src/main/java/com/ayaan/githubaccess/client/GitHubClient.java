package com.ayaan.githubaccess.client;


import com.ayaan.githubaccess.config.GitHubProperties;
import com.ayaan.githubaccess.dto.CollaboratorDto;
import com.ayaan.githubaccess.dto.RepositoryDto;
import com.ayaan.githubaccess.exception.GitHubApiException;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;


@Component
public class GitHubClient {


    private static final Logger log =
            LoggerFactory.getLogger(GitHubClient.class);


    private final RestTemplate restTemplate;

    private final GitHubProperties gitHubProperties;



    public GitHubClient(RestTemplate restTemplate,
                        GitHubProperties gitHubProperties) {

        this.restTemplate = restTemplate;
        this.gitHubProperties = gitHubProperties;
    }




    public List<RepositoryDto> fetchRepositories() {


        log.info("Fetching repositories for organization: {}",
                gitHubProperties.getOrganization());



        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(
                gitHubProperties.getToken()
        );

        headers.setAccept(
                List.of(MediaType.APPLICATION_JSON)
        );


        HttpEntity<Void> entity =
                new HttpEntity<>(headers);



        String url =
                "https://api.github.com/orgs/"
                + gitHubProperties.getOrganization()
                + "/repos";



        try {


            ResponseEntity<RepositoryDto[]> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            RepositoryDto[].class
                    );


            List<RepositoryDto> repositories =
                    Arrays.asList(response.getBody());



            log.info(
                    "Repositories fetched successfully. Count: {}",
                    repositories.size()
            );


            return repositories;



        } catch (HttpClientErrorException e) {


            log.error(
                    "GitHub client error while fetching repositories: {}",
                    e.getStatusCode()
            );


            throw new GitHubApiException(
                    "GitHub API Error : " + e.getStatusCode(),
                    e.getStatusCode().value()
            );



        } catch (HttpServerErrorException e) {


            log.error(
                    "GitHub server error while fetching repositories"
            );


            throw new GitHubApiException(
                    "GitHub Server is unavailable",
                    503
            );
        }

    }






    public List<CollaboratorDto> fetchCollaborators(String repoName) {


        log.info(
                "Fetching collaborators for repository: {}",
                repoName
        );



        HttpHeaders headers = new HttpHeaders();


        headers.setBearerAuth(
                gitHubProperties.getToken()
        );


        headers.setAccept(
                List.of(MediaType.APPLICATION_JSON)
        );



        HttpEntity<Void> entity =
                new HttpEntity<>(headers);




        String url =
                "https://api.github.com/repos/"
                + gitHubProperties.getOrganization()
                + "/"
                + repoName
                + "/collaborators";




        try {


            ResponseEntity<CollaboratorDto[]> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            CollaboratorDto[].class
                    );



            List<CollaboratorDto> collaborators =
                    Arrays.asList(response.getBody());



            log.info(
                    "Collaborators fetched successfully for repo: {} Count: {}",
                    repoName,
                    collaborators.size()
            );



            return collaborators;



        } catch (HttpClientErrorException e) {


            log.error(
                    "GitHub client error while fetching collaborators for repo {} : {}",
                    repoName,
                    e.getStatusCode()
            );



            throw new GitHubApiException(
                    "GitHub API Error : " + e.getStatusCode(),
                    e.getStatusCode().value()
            );



        } catch (HttpServerErrorException e) {


            log.error(
                    "GitHub server error while fetching collaborators for repo {}",
                    repoName
            );



            throw new GitHubApiException(
                    "GitHub Server is unavailable",
                    503
            );
        }

    }
    @Async
    public CompletableFuture<List<CollaboratorDto>> fetchCollaboratorsAsync(
            String repoName) {


        return CompletableFuture.supplyAsync(() -> {

            return fetchCollaborators(repoName);

        });

    }

}