# GitHub Access Report Service

## Overview

GitHub Access Report Service is a Spring Boot REST API that integrates with the GitHub REST API to generate an access report for a GitHub organization.

The application retrieves all repositories of an organization, fetches collaborators for every repository, and generates an aggregated report showing which users have access to which repositories along with their permission levels.

This project was built with scalability and clean architecture in mind by using asynchronous API calls, proper exception handling, reusable components, and layered architecture.

---

## Features

- GitHub Personal Access Token Authentication
- Fetch repositories from a GitHub organization
- Fetch collaborators of every repository
- Generate user-wise repository access report
- Asynchronous API calls using CompletableFuture
- Global Exception Handling
- Logging using SLF4J
- Clean Layered Architecture
- JSON REST API

---

## Tech Stack

- Java 21
- Spring Boot 4.1
- Maven
- RestTemplate
- GitHub REST API
- Lombok
- SLF4J

---

## Project Structure

src/main/java

├── controller

├── service

├── client

├── dto

├── config

├── exception

└── application

Each layer has a single responsibility.

Controller handles API requests.

Service contains business logic.

Client communicates with GitHub REST API.

DTOs transfer data between layers.

Config contains application configuration.

Exception package handles custom exceptions globally.

---

## Authentication

The application authenticates with GitHub using a Personal Access Token.

Configure the following properties inside application.properties.

```properties
github.organization=spring-projects
github.token=YOUR_GITHUB_TOKEN
```

The Personal Access Token should have permission to read repository information and collaborators.

---

## How to Run

### Clone the repository

```bash
git clone https://github.com/your-username/github-access-report-service.git
```

### Move into the project

```bash
cd github-access-report-service
```

### Configure GitHub credentials

Open

```
src/main/resources/application.properties
```

Configure

```properties
server.port=8081

github.organization=spring-projects

github.token=YOUR_GITHUB_TOKEN
```

### Run

Using Maven

```bash
mvn spring-boot:run
```

or simply run

```
GithubAccessReportServiceApplication.java
```

---

## REST API

### Generate Access Report

```
GET /api/access-report
```

Example

```
http://localhost:8081/api/access-report
```

---

## Sample Response

```json
[
  {
    "username": "johndoe",
    "repositories": [
      {
        "name": "spring-boot",
        "permission": "admin"
      },
      {
        "name": "spring-security",
        "permission": "write"
      }
    ]
  }
]
```

---

## Error Handling

The application uses a Global Exception Handler.

Example Response

```json
{
  "timestamp":"2026-07-19T12:00:00",
  "status":404,
  "message":"GitHub API Error : 404 NOT_FOUND"
}
```

---

## Design Decisions

### Layered Architecture

The application follows a layered architecture where every layer has a single responsibility.

### Reusable GitHub Client

All communication with GitHub REST API is centralized inside GitHubClient to improve maintainability.

### Asynchronous Processing

Collaborator information is fetched asynchronously using CompletableFuture to reduce overall response time when organizations contain many repositories.

### Thread Safety

A synchronized block is used while updating the shared access report map to avoid concurrent modification issues.

### Exception Handling

Custom exceptions and a global exception handler ensure consistent JSON error responses.

---

## Scalability

The service is designed considering organizations with a large number of repositories and collaborators.

Current implementation supports asynchronous GitHub API calls.

Future improvements include pagination support and caching.

---

## Future Improvements

- GitHub API Pagination
- Swagger/OpenAPI Documentation
- Caching
- Unit Testing using JUnit and Mockito
- Docker Support
- GitHub Actions CI/CD

---

## Author

Ayaan Anwar

Java Backend Developer
