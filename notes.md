install `psql` client inside a docker container?

```
apt-get update
apt-get install postgresql-client
psql -h postgres -p 5432 -U postgres
```


```
export POSTGRES_HOST=dockerhost
export POSTGRES_PORT=5431
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=postgres
export POSTGRES_DB=scrabble_helper
```