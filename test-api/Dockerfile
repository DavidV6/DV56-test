# Build stage 0
FROM maven:3.6.3-openjdk-8-slim

WORKDIR /buildroot

COPY . ./

RUN mvn clean install

# Build stage 1
FROM openjdk:8-alpine

WORKDIR /app

COPY .docker/config.yml /app/config.yml

COPY --from=0 "/buildroot/target/test-api.jar" /app/app.jar

CMD java -jar app.jar server config.yml
