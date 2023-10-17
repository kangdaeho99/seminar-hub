#!/bin/bash

REPOSITORY=/home/ec2-user/app/repo
PROJECT_NAME=seminar-hub

cd $REPOSITORY/$PROJECT_NAME/



echo "> Git Pull"

git fetch --all && git reset --hard origin/master && git pull

echo "> Project Build Started"

cp /home/ec2-user/app/application-real-db.properties application-real-db.properties 

chmod +x ./gradlew

SPRING_PROFILES_ACTIVE=real-db ./gradlew build

echo "> Move to step1 directory"

cd $REPOSITORY

echo "> Build File Copy"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> Check Processing Current Application PID"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "Current Application pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> There is no Current Processing Application, so not Terminate."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> new Application Deploy"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

nohup java -jar \
        -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-real-db.properties,classpath:/application-real.properties \
        -Dspring.profiles.active=real \
        $REPOSITORY/$JAR_NAME 2>&1 &
