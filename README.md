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
