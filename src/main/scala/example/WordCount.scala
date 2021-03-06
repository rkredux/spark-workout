package org.rkredux.application
package example

import org.apache.spark._
import org.apache.log4j._

object WordCount {
  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCount")
    println("The class of object from SparkContent is: " + sc.getClass)

    val input = sc.textFile("/Users/rahulkumar/Desktop/mppsbt/src/main/scala/example/data/book.txt")
    println("The class of object generated by reading the input file is: " + input.getClass)
//    The class of object generated by reading the input file is: class org.apache.spark.rdd.MapPartitionsRDD
    println("The number of partitions is: " + input.getNumPartitions)

    // Split into words separated by a space character
    val words = input.flatMap(x => x.split(" "))
    println("The class of object generated by a flatMap is: " + words.getClass)
//    The class of object generated by a flatMap is: class org.apache.spark.rdd.MapPartitionsRDD

    // Count up the occurrences of each word
    val wordCounts = words.countByValue()
    println(wordCounts)
    println("The class of object generated by a countByValue is: " + wordCounts.getClass)
//    The class of object generated by a countByValue is: class scala.collection.immutable.HashMap$HashTrieMap
    // Print the results.
    wordCounts.foreach(println)
  }
}
