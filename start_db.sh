#!/bin/sh

cd ./.docker/tickitdb

DB_FIXTURE_PATH=$(pwd)

cd -

docker run -p 5432:5432 \
          -e POSTGRES_PASSWORD=demopassword \
          -e POSTGRES_USER=demouser \
          -e POSTGRES_DB=demo \
          -v /$DB_FIXTURE_PATH:/data \
          library/postgres:9.6-alpine
