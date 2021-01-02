package org.rkredux.application
package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.log4j._

object AvgFriendsByAgeD {

  Logger.getLogger("org").setLevel(Level.ERROR)
  case class Person(id: Int, name: String, age: Int, numberOfFriends: Int)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("AvgFriendsByAge")
      .master("local[*]")
      .getOrCreate()

    //if header = false then have to supply this else columns get inferred.
    // TODO see if this can be made dependent on the case class declared above
    val personSchema = StructType(Array(
      StructField("id", IntegerType, true),
      StructField("name", StringType, true),
      StructField("age", IntegerType, true),
      StructField("numberOfFriends", IntegerType, true))
    )

    import spark.implicits._
    val schemaPerson = spark.read // this creates a df reader object
      .option("header", "false") //because there is no header in the csv
      //.option("inferSchema", "true") //without any header this will give _c0, _c1,...
      .schema(personSchema) //this is basically giving it the header and
      // the names have to be the same as case class if converting to a dataset
      .csv("/Users/rahulkumar/Desktop/mppsbt/src/main/scala/example/data/fakefriends.csv") //this returns a df
      .as[Person] //this is the line that converts the df to a dataset now type safety comes into picture

    schemaPerson.groupBy("age")
      .avg("numberOfFriends")
      .orderBy("avg(numberOfFriends)")
      .withColumnRenamed("avg(numberOfFriends)", "avgFriends")
      .show()
    spark.stop()
  }

}
