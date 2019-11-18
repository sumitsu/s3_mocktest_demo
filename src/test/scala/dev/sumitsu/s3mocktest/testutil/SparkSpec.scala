package dev.sumitsu.s3mocktest.testutil

import dev.sumitsu.s3mocktest.util.Logging
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait SparkSpec {

  this: Logging =>

  import SparkSpec._

  val spark: SparkSession = {
    val sparkConf = new SparkConf()
      .set("spark.driver.host", IpAddrSparkMaster)
      .setMaster("local[2]")
      .setAppName(LogClassName)
    val sparkSession = SparkSession.builder.config(sparkConf).getOrCreate
    sparkSession.sql("set spark.sql.caseSensitive=true")
    sparkSession
  }

}

object SparkSpec {
  val IpAddrSparkMaster: String = "127.0.0.1"
  val SparkDriverMemory: String = "2G"
  val SparkDriverMaxResultSize: String = "1G"
  val SparkExecutorMemory: String = "2G"
}
