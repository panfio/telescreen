#!/bin/bash
#This script builds artifacts and docker container

# export MAVEN_OPTS=-Dorg.slf4j.simpleLogger.defaultLogLevel=warn

#build 
cd ./frontend 
npm install
npm run-script build
cd ..

#build gateway service
rm -rf ./telescreen/src/main/resources/static/*
cp -r ./frontend/build/* ./telescreen/src/main/resources/static/
cd ./telescreen
./mvnw -B clean install package
docker build --tag panfio/telescreen:gateway-latest .
cd ..

#build handler service
cd ./handler
#./mvnw -B clean install package
docker build --tag panfio/telescreen:handler-latest .
cd ..

#build data service
cd ./data
./mvnw -B clean install package
docker build --tag panfio/telescreen:data-latest .
cd ..

#build admin service
cd ./admin
./mvnw -B clean install package
docker build --tag panfio/telescreen:admin-latest .
cd ..

# java -jar ./admin/target/*.jar
# java -jar ./telescreen/target/*.jar
# java -jar ./data/target/*.jar
# java -jar ./handler/target/*.jar



