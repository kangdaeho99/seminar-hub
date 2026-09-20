FROM openjdk:17-jdk-slim
COPY /build/libs/*.jar seminar-hub-0.0.1-SNAPSHOT.jar
COPY /src/main/resources/application-real-db.properties /home/ec2-user/app/application-real-db.properties
ENTRYPOINT ["java","-Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-real-db.properties,classpath:/application-real.properties", "-Dspring.profiles.active=real","-jar", "/seminar-hub-0.0.1-SNAPSHOT.jar"]

#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]