package ru.dvi.flink.test

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.api.scala._
import org.apache.flink.configuration.MemorySize
import org.apache.flink.connector.file.sink.FileSink
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import java.time.Duration
import org.apache.flink.formats.compress.CompressWriterFactory._

object MainTopicToFileForBulkFormat {


  val brokers = "localhost:9093"
  val topic = "test"
  val groupId = "flink"
  val outputPath = ".simple/out.txt"

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.createLocalEnvironment()

    val source: KafkaSource[String] = KafkaSource.builder[String]()
      .setBootstrapServers(brokers)
      .setTopics(topic)
      .setGroupId(groupId)
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .build();

    //    CompressWriters.forExtractor(new DefaultExtractor()).withHadoopCompression("GzipCodec")

    val sink: FileSink[String] = FileSink
      .forBulkFormat(new Path(outputPath),
        new CompressWriters.forExtractor(new DefaultExtractor()).withHadoopCompression("GzipCodec"))
      .withRollingPolicy(
        DefaultRollingPolicy.builder()
          .withRolloverInterval(Duration.ofMinutes(15))
          .withInactivityInterval(Duration.ofMinutes(5))
          .withMaxPartSize(MemorySize.ofMebiBytes(1))
          .build())
      .build()

    val text = env.fromSource(source, WatermarkStrategy.noWatermarks[String], "Kafka Source")
      .filter(x => {
        x.nonEmpty
      })
      .map(x => {
        x
      })


    text.sinkTo(sink)

    env.execute("Window Stream WordCount")

  }
}