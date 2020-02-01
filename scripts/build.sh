#!/bin/bash
#This script builds artifacts and docker container

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
docker build --tag panfio/telescreen:latest .
cd ..

#build handler service
cd ./handler
./mvnw -B clean install package
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





