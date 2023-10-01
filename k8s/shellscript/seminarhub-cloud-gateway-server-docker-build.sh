#!/bin/bash

REPOSITORY=/root/app/repo
PROJECT_NAME=seminar-hub


cd $REPOSITORY/$PROJECT_NAME/
echo "> Git Pull"
git fetch --all && git reset --hard origin/master && git pull
echo "> Project Build Started"
chmod +x ./gradlew
chmod 755 $REPOSITORY/$PROJECT_NAME

cd $REPOSITORY/$PROJECT_NAME
./gradlew seminarhub-cloud-gateway-server:build -x test
sudo mkdir -p ./seminarhub-cloud-gateway-server/src/main/resources
sudo cp $REPOSITORY/seminarhub-cloud-gateway-server-application.yml $REPOSITORY/$PROJECT_NAME/seminarhub-cloud-gateway-server/src/main/resources/application.yml
docker rmi eoghdhdls/seminarhub-cloud-gateway-server
cd $REPOSITORY/$PROJECT_NAME/seminarhub-cloud-gateway-server
docker build -t eoghdhdls/seminarhub-cloud-gateway-server .
docker push eoghdhdls/seminarhub-cloud-gateway-server
