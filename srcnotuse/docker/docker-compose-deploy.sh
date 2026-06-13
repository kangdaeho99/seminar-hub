#!/bin/bash

REPOSITORY=/home/ec2-user/app/repo
PROJECT_NAME=seminar-hub

cd $REPOSITORY/$PROJECT_NAME/


echo "> Git Pull"

git fetch --all && git reset --hard origin/master && git pull

echo "> Project Build Started"

chmod +x ./gradlew
chmod 755 $REPOSITORY/$PROJECT_NAME

docker-compose down

cd $REPOSITORY/$PROJECT_NAME/
./gradlew seminarhub-cloud-config-server:build
sudo mkdir -p ./seminarhub-cloud-config-server/src/main/resources
sudo cp $REPOSITORY/seminarhub-cloud-config-server-application.yml $REPOSITORY/$PROJECT_NAME/seminarhub-cloud-config-server/src/main/resources/application.yml
docker rmi seminarhub-cloud-config-server
cd $REPOSITORY/$PROJECT_NAME/seminarhub-cloud-config-server
docker build -t seminarhub-cloud-config-server .



cd $REPOSITORY/$PROJECT_NAME
./gradlew seminarhub-cloud-gateway-server:build -x test
sudo mkdir -p ./seminarhub-cloud-gateway-server/src/main/resources
sudo cp $REPOSITORY/seminarhub-cloud-gateway-server-application.yml $REPOSITORY/$PROJECT_NAME/seminarhub-cloud-gateway-server/src/main/resources/application.yml
docker rmi seminarhub-cloud-gateway-server
cd $REPOSITORY/$PROJECT_NAME/seminarhub-cloud-gateway-server
docker build -t seminarhub-cloud-gateway-server .



cd $REPOSITORY/$PROJECT_NAME
./gradlew seminarhub-cloud-netflix-eureka-server:build -x test
sudo mkdir -p ./seminarhub-cloud-netflix-eureka-server/src/main/resources
sudo cp $REPOSITORY/seminarhub-cloud-netflix-eureka-server-application.yml $REPOSITORY/$PROJECT_NAME/seminarhub-cloud-netflix-eureka-server/src/main/resources/application.yml
docker rmi seminarhub-cloud-netflix-eureka-server
cd $REPOSITORY/$PROJECT_NAME/seminarhub-cloud-netflix-eureka-server
docker build -t seminarhub-cloud-netflix-eureka-server .


cd $REPOSITORY/$PROJECT_NAME
./gradlew seminarhub-member-api-server:build -x test
sudo mkdir -p ./seminarhub-member-api-server/src/main/resources
sudo cp $REPOSITORY/seminarhub-member-api-server-application.yml $REPOSITORY/$PROJECT_NAME/seminarhub-member-api-server/src/main/resources/application.yml
docker rmi seminarhub-member-api-server
cd $REPOSITORY/$PROJECT_NAME/seminarhub-member-api-server
docker build -t seminarhub-member-api-server .



cd $REPOSITORY/$PROJECT_NAME
./gradlew seminarhub-seminar-api-server:build -x test
sudo mkdir -p ./seminarhub-seminar-api-server/src/main/resources
sudo cp $REPOSITORY/seminarhub-seminar-api-server-application.yml $REPOSITORY/$PROJECT_NAME/seminarhub-seminar-api-server/src/main/resources/application.yml
docker rmi seminarhub-seminar-api-server
cd $REPOSITORY/$PROJECT_NAME/seminarhub-seminar-api-server
docker build -t seminarhub-seminar-api-server .



cd $REPOSITORY/$PROJECT_NAME
./gradlew seminarhub-member-seminar-api-server:build -x test
sudo mkdir -p ./seminarhub-member-seminar-api-server/src/main/resources
sudo cp $REPOSITORY/seminarhub-member-seminar-api-server-application.yml $REPOSITORY/$PROJECT_NAME/seminarhub-member-seminar-api-server/src/main/resources/application.yml
docker rmi seminarhub-member-seminar-api-server
cd $REPOSITORY/$PROJECT_NAME/seminarhub-member-seminar-api-server
docker build -t seminarhub-member-seminar-api-server .


docker-compose up -d
