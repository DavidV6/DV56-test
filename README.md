# Note

If you have any issues setting up any part of our test, please let us know so that we can assist.

# Prerequisites

Requires `docker engine 19.03.0+`.

Docker Mac Install: https://hub.docker.com/editions/community/docker-ce-desktop-mac/

Docker Windows Install: https://hub.docker.com/editions/community/docker-ce-desktop-windows/

Docker Linux Install, follow instructions for your dist: https://docs.docker.com/engine/install/

## Setting up the environment

### Before starting the stack

Requires `java 8+`, `maven 3.6+` and `node 13+`.
Every command assume you are running them from the project root, either in linux or macOS.

Run `cd test-client; npm install`.

Run `cd test-api; mvn clean install`.

### running the stack

#### PostgreSQL

Start PostgreSQL using `start_db.sh` in sehll.

#### Server Java

Once PostgreSQL is running, start `test-api` using `test-api; java -jar target/test-api.jar server src/main/resources/config.yml`
Every new build of `test-api` will require a restart of `test-api`.

#### Application start

In a new command line start the client app using `cd test-client; npm start`.
The client app will be available on `http://localhost:3000/`.
