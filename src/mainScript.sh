#!/bin/bash
numOfProcessToCreate=$1
bindingLoc=$(2:-"localhost")
isServer=$(3:-0)
minNumProcs=$(4:-0)

if [isServer != 0];then
    echo "Starting Server"
    javac ServerMain.java
    java ServerMain minNumProcs &
    echo "Server Started"
    bindingLoc="localhost"
fi

echo "Now Compiling Program"
javac Main.java
echo "Progarm compiled Now Starting Execution"

echo "Starting Script for " $numOfProcces" Processes"
for ((i=0;i<numOfProcessToCreate;i++));
do
	java Main bindingLoc &
done
read -p "Press Any Key to Exit"
kill $(ps aux | grep '[j]ava Main' | awk '{print $2}')
kill $(ps aux | grep '[j]ava ServerMain' | awk '{print $2}')
