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

## Jenkins: three chained jobs

The `jenkins` folder contains three independent Pipeline scripts. Create three
Jenkins **Pipeline** jobs using **Pipeline script from SCM**. Point each job at
this repository, select the `main` branch, and set its Script Path as follows:

| Jenkins job name | Script Path |
| --- | --- |
| `01-build-jar` | `jenkins/Jenkinsfile.01-build-jar` |
| `02-build-run-image` | `jenkins/Jenkinsfile.02-build-run-image` |
| `03-deploy-kubernetes` | `jenkins/Jenkinsfile.03-deploy-kubernetes` |

The jobs trigger one another in order and wait for each downstream job to
succeed. Job 1 runs Maven tests/package and archives the JAR. Job 2 copies that
exact build's JAR, builds `Dockerfile.app` with the same commit, runs a
temporary container and checks the home endpoint, then pushes the image tagged
with that commit SHA. It removes the temporary container even if the build
fails. Job 3 applies the Kubernetes manifests with that exact image and waits
for the Deployment rollout.

Before running the jobs:

1. Install the Jenkins **Pipeline**, **Git**, **Copy Artifact**, and
   **Credentials Binding** plugins.
2. Ensure the Jenkins agent has Java 17, Maven, Docker CLI with access to a
   running Docker engine, PowerShell, and `kubectl`. Configure all three jobs
   to run on an agent with the required tools and access.
3. Add a Jenkins **Username with password** credential with ID
   `dockerhub-creds`. The Docker Hub account must be able to push to the
   repository supplied by the `DOCKERHUB_REPOSITORY` job parameter. Use a
   public Docker Hub repository so Kubernetes can pull the image without
   additional cluster credentials.
4. Add a Jenkins **Secret file** credential with ID `kubeconfig`. It must be a
   kubeconfig for the target cluster, usable from the Jenkins agent. The
   cluster must be reachable from that agent.
5. Run `01-build-jar` and set `DOCKERHUB_REPOSITORY` to your Docker Hub
   `namespace/repository` (for example, `myaccount/cicd-demo`). Set
   `K8S_NAMESPACE` if you do not want the `default` namespace.

The placeholder repository value is intentionally not a usable Docker Hub
repository. Do not put registry passwords or kubeconfig contents in Pipeline
files or job parameters.
