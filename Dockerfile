FROM openjdk:21-jdk-slim AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml /app
COPY src /app/src
COPY test /app/test

RUN mvn clean package

FROM openjdk:21
WORKDIR /app
ADD target/AccountManagementService-1.0-SNAPSHOT-jar-with-dependencies.jar add.jar
ENTRYPOINT ["java", "-jar", "add.jar"]