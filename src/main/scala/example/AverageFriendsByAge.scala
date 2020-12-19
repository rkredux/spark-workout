package org.rkredux.application
package example

import org.apache.spark._
import org.apache.log4j._

object AverageFriendsByAge {

  def parseLine(line: String): (Int, Int) = {
    val lineSplit = line.split(",")
    val age = lineSplit(2).toInt
    val numFriends = lineSplit(3).toInt
    (age, numFriends)
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "AverageFriendsByAge")
    val input = sc.textFile("/Users/rahulkumar/Desktop/mppsbt/src/main/scala/example/data/fakefriends.csv")
    val rdd = input.map((line) => parseLine(line)) //(age, friendsCount)
    val rdd1 = rdd.mapValues(x => (x, 1)).reduceByKey((x,y) => (x._1 + y._1, x._2 + y._2)) //(age, (totalFriends,count))
    val rdd2 =  rdd1.mapValues((x) => x._1 /x._2) //(age, AvgFriendsByAge)
    val results = rdd2.collect()
    results.sorted.foreach(println)
  }
}
