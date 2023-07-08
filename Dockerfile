FROM openjdk:17-jdk-slim
COPY /build/libs/*.jar seminar-hub-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=real","-jar", "/seminar-hub-0.0.1-SNAPSHOT.jar"]

#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]