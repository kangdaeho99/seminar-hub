FROM openjdk:17-jdk-alpine
COPY /build/libs/*.jar seminarhub-cloud-netflix-eureka-server-0.0.1-SNAPSHOT.jar
COPY /src/main/resources/application.yml /application.yml
ENTRYPOINT ["java", "-jar", "/seminarhub-cloud-netflix-eureka-server-0.0.1-SNAPSHOT.jar",  "--spring.config.location=classpath:/application.yml"]