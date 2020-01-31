#!/bin/bash
#This script builds artifacts and docker container

#build 
cd ./frontend 
npm install
npm run-script build
cd ..

#build
rm -rf ./telescreen/src/main/resources/static/*
cp -r ./frontend/build/* ./telescreen/src/main/resources/static/
cd ./telescreen
./mvnw -B clean install package
cd ..
docker build --tag panfio/telescreen .

#build
cd ./data
./mvnw -B clean install package
docker build --tag panfio/telescreen:data-latest .






