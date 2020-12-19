package org.rkredux.application
package example

import org.apache.spark._
import org.apache.log4j._

object RatingsCounter {
  def main(args: Array[String]): Unit = {
//    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]","RatingsCounter" )
    val ratingsData = sc.textFile("/Users/rahulkumar/Desktop/mppsbt/src/main/scala/example/data/ratings.txt")
    val header = ratingsData.first()
    val ratingsDataWithoutHeader = ratingsData.filter(row => row != header)
    val ratingsValue = ratingsDataWithoutHeader.map(x => x.split(",")(2))
    val results = ratingsValue.countByValue()
    val sortedResults = results.toSeq.sortBy(_._1)
    sortedResults.foreach(println)
  }
}
