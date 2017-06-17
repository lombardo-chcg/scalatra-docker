#!/bin/bash

tag=${1:-latest}

echo -e "compiling project...\n"

sbt assembly

docker build -t lombardo/scrabble-helper-api:$tag .