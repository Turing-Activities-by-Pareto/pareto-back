# Backend Repository

This repository contains the backend codes.

## Backend

Here, the technologies and structures used for the backend are described.

### Installation

```bash
git clone https://github.com/Turing-Activities-by-Pareto/pareto-back.git
cd backend
```

### Push the newest app to the Docker Hub

```bash
DOCKER_PASSWORD=PASSWORD DOCKER_USERNAME=mr3iscuit ./gradlew clean build jib 
```

### Running the Backend in local environment

```bash
docker compose up
```

### Stop all of the services

```bash
docker compose down
```

### API Endpoints

#### Default Swagger URL
```bash
http://localhost:8080/swagger-ui/index.html
```
