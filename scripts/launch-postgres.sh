#!/bin/bash

db_container="postgres"

docker-compose down
docker-compose up -d

docker cp db/create_greetings_table.sql $db_container:/docker-entrypoint-initdb.d/create_greetings_table.sql
docker cp db/seed_greetings_table.sql $db_container:/docker-entrypoint-initdb.d/seed_greetings_table.sql

docker exec -d \
  $db_container psql \
  --username=postgres \
  -f ./docker-entrypoint-initdb.d/create_greetings_table.sql

docker exec -d \
  $db_container psql \
  --username=postgres \
  -f ./docker-entrypoint-initdb.d/seed_greetings_table.sql