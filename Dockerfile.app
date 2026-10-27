FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
RUN useradd --system --uid 10001 --create-home appuser
COPY target/*.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
