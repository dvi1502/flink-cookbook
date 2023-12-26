package ru.dvi.flink.test

import org.apache.flink.api.scala._

object MainSimpleText {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    // get input data
    val text = env.fromElements("To be, or not to be,--that is the question:--",
      "Whether 'tis nobler in the mind to suffer", "The slings and arrows of outrageous fortune",
      "Or to take arms against a sea of troubles,")

    val counts = text
      .flatMap(x => {
        x.toLowerCase.split("\\\\W+")
      })
      .map(x=> {
        (x, 1)
      })
      .groupBy(0)
      .sum(1)

    // execute and print result
    counts.print()
  }
}