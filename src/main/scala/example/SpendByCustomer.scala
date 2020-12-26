package org.rkredux.application
package example

import org.apache.spark._
import org.apache.log4j._

object SpendByCustomer {
  def parseLine(line: String) = {
    val split = line.split(",")
    val customerId = split(0)
    val orderAmount = split(2).toFloat
    (customerId, orderAmount)
  }
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "SpendByCustomer")
    val input = sc.textFile("/Users/rahulkumar/Desktop/mppsbt/src/main/scala/example/data/customer-orders.csv")
    val parsedInput = input.map(line => parseLine(line)) //(customerId, amount)
    val orderByCustomer = parsedInput.reduceByKey((x,y) => x + y)
    val results = orderByCustomer.collect()
    results.sorted.foreach(println)
  }
}
