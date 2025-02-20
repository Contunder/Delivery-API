FROM eclipse-temurin:21-jammy
WORKDIR /opt/app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
