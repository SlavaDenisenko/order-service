#!/bin/bash

function build_basic() {
    JAR_FILE=$1
    APP_NAME=$2

    docker build -f ./build-scripts/docker/basic/Dockerfile \
      --build-arg JAR_FILE=${JAR_FILE} \
      -t ${APP_NAME}:1.0 .

    docker tag ${APP_NAME}:1.0 sdenisenko/${APP_NAME}:1.0

    docker push sdenisenko/${APP_NAME}:1.0
}

APP_VERSION=1.0-SNAPSHOT

# Building the app
cd ..

echo "Building JAR files"
mvn clean package -DskipTests

echo "Building Docker images"
build_basic ./billing-service/target/billing-service-${APP_VERSION}.jar billing-service
build_basic ./notification-service/target/notification-service-${APP_VERSION}.jar notification-service
build_basic ./order-service/target/order-service-${APP_VERSION}.jar order-service