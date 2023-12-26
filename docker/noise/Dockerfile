FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY noise-gen-producer.jar ./
COPY start.sh ./

RUN chmod 777 ./noise-gen-producer.jar; \
    chmod 777 ./start.sh;

RUN ls -la ./

ENTRYPOINT ./start.sh