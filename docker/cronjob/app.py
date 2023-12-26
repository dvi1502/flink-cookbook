#!/usr/bin/python3

from kafka import KafkaConsumer, TopicPartition, OffsetAndMetadata, KafkaProducer
from datetime import datetime
import os
import sys
import time

BOOSTRAP_SERVER = os.getenv('KAFKA_BOOSTRAP_SERVER', 'kafka:9092')
TOPIC_NAME = os.getenv('KAFKA_TOPIC', 'test')
MESSAGE_KEY = os.getenv('KAFKA_MESSAGE_KEY', '')
if len(sys.argv) == 1:
    MESSAGE_VALUE = os.getenv('KAFKA_MESSAGE_VALUE', '100;message10;message100')
else:
    MESSAGE_VALUE = sys.argv[1]

print(BOOSTRAP_SERVER.split(","))
print(TOPIC_NAME)
print(MESSAGE_KEY)
print(MESSAGE_VALUE)

def job():

    print("I'm working...")
    print("producing message... ")
    producer.send(TOPIC_NAME, key=MESSAGE_KEY.encode('utf-8'), value=MESSAGE_VALUE.encode('utf-8')).get(timeout=30)
    print("produce message success!")


producer = KafkaProducer(bootstrap_servers=BOOSTRAP_SERVER.split(","))

while True:
    print('running backup')
    job()
    time.sleep(3)









