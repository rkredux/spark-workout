package org.rkredux.application
package example

import org.apache.spark._
import org.apache.log4j._

object MinTemp {

  def parseLine(line: String) ={
    val lineSplit = line.split(",")
    val stationId = lineSplit(0)
    val observationType = lineSplit(2)
    val measurement = lineSplit(3).toFloat
    (stationId,observationType,measurement)
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "MinTemp")
    val input = sc.textFile("/Users/rahulkumar/Desktop/mppsbt/src/main/scala/example/data/1800.csv")
    val minTemperatureObservations = input.map(line => parseLine(line)).filter(input => input._2 == "TMIN")
    val stationTemp = minTemperatureObservations.map(x => (x._1, x._3.toFloat)) //tuple of key value pair
    val minTempsByStation = stationTemp.reduceByKey((x,y) => if (x < y) x else y ) //use reduce by key
    val results = minTempsByStation.collect() //bring the result back to the master
    for (result <- results.sorted) {
      val station = result._1
      val temp = result._2
      val formattedTemp = f"$temp%.2f F"
      println(s"$station minimum temperature: $formattedTemp")
    }
  }

}
