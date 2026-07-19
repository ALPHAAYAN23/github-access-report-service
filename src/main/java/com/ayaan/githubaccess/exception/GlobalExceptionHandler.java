package com.ayaan.githubaccess.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex){

        ApiErrorResponse response =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        500,
                        ex.getMessage()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    @ExceptionHandler(GitHubApiException.class)
    public ResponseEntity<ApiErrorResponse> handleGitHubException(
            GitHubApiException ex
    ){

        ApiErrorResponse response =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        ex.getStatusCode(),
                        ex.getMessage()
                );


        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }

}