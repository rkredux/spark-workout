package org.rkredux.application
package example

import org.apache.spark._

object SquareIt {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local[*]", "squareIt")
    val distData = sc.parallelize(Array(1,2,3,4,5,6))
    def sqr(x: Int) = x * x
    val squareData = distData.map(sqr)
    println(squareData.reduce((x,y) => x + y))
  }
}
