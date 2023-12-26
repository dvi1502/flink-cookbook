#!/bin/sh

echo $KAFKA_BOOSTRAP_SERVER
echo $KAFKA_TOPIC
echo $KAFKA_MESSAGE_VALUE

java -cp /app/noise-gen-producer.jar \
ru.beeline.dmp.noise.NoiseToKafkaWithCron \
--cron "$CRON_SCHEDULER" \
--url $KAFKA_BOOSTRAP_SERVER \
--topic $KAFKA_TOPIC \
--pattern $KAFKA_MESSAGE_VALUE \
--count $BATCH_SIZE;

