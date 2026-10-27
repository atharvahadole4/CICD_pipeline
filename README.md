# GitLab CI/CD Spring Boot Demo

A small Spring Boot REST API with two GET routes: a home message and a route for
demonstrating the `start`, `stop`, and `hold` pipeline states.

## Run locally

Requires Java 17 and Maven.

```powershell
mvn spring-boot:run
```

## Endpoints

| Method | Path | Response |
| --- | --- | --- |
| GET | `/` | `GitLab CI/CD home program` |
| GET | `/pipeline/start` | `CI/CD pipeline started` |
| GET | `/pipeline/stop` | `CI/CD pipeline stopped` |
| GET | `/pipeline/hold` | `CI/CD pipeline on hold` |

Unsupported pipeline actions return HTTP 400.

The `.gitlab-ci.yml` pipeline runs the tests and packages the executable JAR.

## Run with Docker Compose

Build the image and start the API from the project directory:

```powershell
docker compose up --build
```

The API is available at `http://localhost:8080`. Stop it with:

```powershell
docker compose down
```

The multi-stage `Dockerfile` builds the Spring Boot JAR and runs it as a
non-root user. GitLab CI builds and pushes an image tagged with the commit SHA
to the project's Container Registry; commits on the default branch are also
tagged `latest`. The GitLab Runner must support Docker-in-Docker and be
configured for privileged service containers.

## Deploy to Kubernetes

Update the image in `k8s/deployment.yaml` to the image path in your GitLab
Container Registry, then apply both manifests:

```powershell
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

The Service is internal to the cluster (`ClusterIP`). For local access, run:

```powershell
kubectl port-forward service/gitlab-cicd-demo 8080:80
```
