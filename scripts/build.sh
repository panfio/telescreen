#!/bin/bash
#This script builds artifacts and docker container

#build 
cd ./frontend 
npm install
npm run-script build
cd ..

#build server
rm -rf ./telescreen/src/main/resources/static/*
cp -r ./frontend/build/* ./telescreen/src/main/resources/static/
cd ./telescreen
./mvnw -B clean install package
#java -jar target/telescreen-1.jar


#build container
cd ..
docker build --tag panfio/telescreen .

#docker-compose up 




