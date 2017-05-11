#!/bin/bash


if [[ $(docker-machine active 2>/dev/null) ]]; then
  DOCKER_IP=$(docker-machine ip $(docker-machine active))
else
  DOCKER_IP="localhost"
fi

export POSTGRES_HOST=$DOCKER_IP
export POSTGRES_PORT=5431
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=postgres
export POSTGRES_DB=scrabble_helper

echo $DOCKER_IP

docker-compose down
docker-compose up -d postgres