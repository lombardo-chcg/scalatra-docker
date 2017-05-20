#!/bin/bash

if [[ $(docker-machine active 2>/dev/null) ]]; then
  DOCKER_IP=$(docker-machine ip $(docker-machine active))
else
  DOCKER_IP="localhost"
fi

echo "Docker IP set to $DOCKER_IP"

export POSTGRES_HOST=$DOCKER_IP
export POSTGRES_PORT=5431
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=postgres
export POSTGRES_DB=scrabble_helper
export REDIS_HOST=$DOCKER_IP
export REDIS_PORT=6379

docker-compose down
docker-compose up -d postgres redis