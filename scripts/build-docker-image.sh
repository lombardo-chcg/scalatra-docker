#!/bin/bash

echo -e "compiling project...\n"

sbt assembly

docker build -t lombardo/scrabble-helper-api:latest .