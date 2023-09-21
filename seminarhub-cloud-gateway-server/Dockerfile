FROM openjdk:17-jdk-slim
COPY /build/libs/*.jar seminarhub-cloud-gateway-server-0.0.1-SNAPSHOT.jar
COPY /src/main/resources/application.yml /application.yml
ENTRYPOINT ["java", "-jar", "/seminarhub-cloud-gateway-server-0.0.1-SNAPSHOT.jar", "--spring.config.location=classpath:/application.yml"]

#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]