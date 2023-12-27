package ru.dvi.flink.test

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala._
import org.apache.flink.connector.base.DeliveryGuarantee
import org.apache.flink.connector.file.src.FileSource
import org.apache.flink.connector.file.src.reader.TextLineInputFormat
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import java.time.Duration

object MainTextFileToKafka {

  val brokers = "localhost:9093"
  val topic = "test"
  val groupId = "flink"

  val inputPath = ".simple/part-c000.csv"

  val sink: KafkaSink[String] = KafkaSink
    .builder[String]()
    .setBootstrapServers(brokers)
    .setRecordSerializer(KafkaRecordSerializationSchema.builder()
      .setTopic(topic)
      .setValueSerializationSchema(new SimpleStringSchema())
      .build()
    )
    .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
    .build();

  val source: FileSource[String] =
    FileSource.forRecordStreamFormat(new TextLineInputFormat(), new Path(inputPath))
//      .monitorContinuously(Duration.ofSeconds(1L))
      .build();


  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // get input data
    val text: DataStream[String] =
      env.fromSource(source, WatermarkStrategy.noWatermarks[String], "file-source")

    val stream: DataStream[String] = text
      .flatMap(x => {
        x.toLowerCase.split("\\\\W+")
      })

    stream.sinkTo(sink)

    stream.print()
    env.execute()
  }
}