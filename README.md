# demo-api #

## Build & Run ##

```sh
$ cd demo_api
$ ./sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

install `psql` client inside a docker container?

```
apt-get update
apt-get install postgresql-client
psql -h postgres -p 5432 -U postgres
```


```
export POSTGRES_HOST=dockerhost
export POSTGRES_PORT=5431
```