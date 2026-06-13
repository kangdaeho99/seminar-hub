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
./gradlew seminarhub-member-api-server:build -x test
sudo mkdir -p ./seminarhub-member-api-server/src/main/resources
sudo cp $REPOSITORY/seminarhub-member-api-server-application.yml $REPOSITORY/$PROJECT_NAME/seminarhub-member-api-server/src/main/resources/application.yml
docker rmi eoghdhdls/seminarhub-member-api-server
cd $REPOSITORY/$PROJECT_NAME/seminarhub-member-api-server
docker build -t eoghdhdls/seminarhub-member-api-server .
docker push eoghdhdls/seminarhub-member-api-server
