#!/bin/bash

AUTOTIMER_FOLDER=$1 

cd "$AUTOTIMER_FOLDER"
source env/bin/activate

for (( ; ; )); 
do 
    mv activities.json activities-`date "+%Y%m%d-%H%M%S"`.json; 
    python autotimer.py; 
done;

#(sleep 30) & pid=$!  &&  (sleep 10; kill -9 $pid) &