package ru.dvi.flink.test

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala._
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object MainFile {

  val brokers = "localhost:9093"
  val topic = "test"
  val groupId = "flink"

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.createLocalEnvironment()

    val source:KafkaSource[String] = KafkaSource.builder[String]()
      .setBootstrapServers(brokers)
      .setTopics(topic)
      .setGroupId(groupId)
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .build();


    val text = env.fromSource(source, WatermarkStrategy.noWatermarks[String], "Kafka Source")
    val counts = text
      .flatMap(x => { x.toLowerCase.split("\\\\W+")})
      .filter(x => { x.nonEmpty } )
      .map(x => {
        Tuple2(x, x.size)
      })
      .keyBy(x =>  x._1)
      .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
      .sum(1)

    counts.print()

    env.execute("Window Stream WordCount")

  }
}