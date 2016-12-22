#!/bin/bash
numOfProcessToCreate=$1
RMIBinding=${2:-"localhost"}
infoBinding=${3:-"localhost"}
runInfo=${4:-""}
minNumProcs=${5:-""}

if [ "$runInfo" = "Y" ];then
    echo "Starting Info"
    javac ServerMain.java
    java ServerMain $minNumProcs $runInfo &
    echo "Server Started"
# wait one second for server to start before launching all processes
    sleep 1s
fi

echo "Now Compiling Program"
javac Main.java
echo "Progarm compiled Now Starting Execution"

echo "Starting Script for " $numOfProccesToCreate" Processes"
for ((i=0;i<numOfProcessToCreate;i++));
do
	java Main $RMIBinding $infoBinding &
done
read -p "Press Any Key to Exit"
kill $(ps aux | grep '[j]ava Main' | awk '{print $2}')
kill $(ps aux | grep '[j]ava ServerMain' | awk '{print $2}')
