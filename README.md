# Async Data Processor

This project provides a backend application for managing and processing tasks asynchronously using Java with Spring Boot. It includes endpoints to submit tasks, retrieve all tasks, and get the status of specific tasks.

## Prerequisites

- Docker
- Java 17
- Maven

## Getting Started

### Step 1: Run PostgreSQL Database in Docker

First, ensure you have Docker installed on your machine. Then, run the following command to start a PostgreSQL container:

```sh
docker run -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

### Step 2: Configure Application Properties
Update the application.yml file with the following PostgreSQL configuration

### Step 3: Run the Project Locally
Navigate to the project directory and run the following Maven command to start the application:

```sh
mvn spring-boot:run
```

### Step 4: Access Swagger UI
Once the application is running, open your browser and go to the following URL to access the Swagger UI:

```sh
http://localhost:8080/swagger-ui/index.html
```


## Project Details

### Structure
**api**: Contains the API controllers and DTOs.

**config**: Contains configuration classes.

**domain**: Contains the entity and repository classes.

**exception**: Contains custom exception classes and hanlders.

**service**: Contains the service interfaces and implementations.


### Timeout and Fake Execution Timeout

### Timeout
The **timeoutMs** configuration specifies the maximum time (in milliseconds) that the system will wait for a task to be processed. If the task processing exceeds this time, the task will be marked as failed.

### Fake Execution Timeout
The **fakeExecutionTimeMs** configuration simulates the time (in milliseconds) it takes to process a task. This is used for testing and demonstration purposes.

