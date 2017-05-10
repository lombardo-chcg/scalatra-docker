#!/bin/bash

db_container="postgres"

docker-compose down
docker-compose up -d postgres

docker cp db/dict.csv $db_container:/dict.csv

docker cp db/create_greetings_table.sql $db_container:/docker-entrypoint-initdb.d/create_greetings_table.sql
docker cp db/seed_greetings_table.sql $db_container:/docker-entrypoint-initdb.d/seed_greetings_table.sql
docker cp db/create_words_table.sql $db_container:/docker-entrypoint-initdb.d/create_words_table.sql
docker cp db/seed_words_table.sql $db_container:/docker-entrypoint-initdb.d/seed_words_table.sql
