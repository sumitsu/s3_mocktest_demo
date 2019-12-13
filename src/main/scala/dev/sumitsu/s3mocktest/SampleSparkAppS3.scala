package dev.sumitsu.s3mocktest

import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

class SampleSparkAppS3(spark: SparkSession, bucketName: String) {

  import SampleSparkAppS3._

  val sparkSqlS3URI: String = s"s3://$bucketName/$SparkSqlS3Key"

  def writeDfToS3(): Unit = {
    import spark.implicits._
    val df =
      spark
        .createDataset(spark.sparkContext.parallelize(0 until 500))
        .toDF(WriteColumnName)
    df.write.json(sparkSqlS3URI)
  }

  def readDfFromS3(): DataFrame = {
    spark
      .read
      .schema(StructType(Array(StructField(name = WriteColumnName, dataType = IntegerType))))
      .json(sparkSqlS3URI)
  }

}

object SampleSparkAppS3 {

  /**
   * S3 key/path to use when writing/reading during the SparkSQL->S3 sample app
   */
  val SparkSqlS3Key: String = "SparkSqlS3Test"

  val WriteColumnName: String = "number"

}
