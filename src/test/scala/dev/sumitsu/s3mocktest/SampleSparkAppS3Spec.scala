package dev.sumitsu.s3mocktest

import dev.sumitsu.s3mocktest.testutil.AmazonS3TestUtil.{AwsEndpointUriStr, MockAWSAccessKey, MockAWSSecretKey}
import dev.sumitsu.s3mocktest.testutil.{NonChunkedDefaultS3ClientFactory, S3Spec, SparkSpec}
import org.apache.hadoop.fs.s3a.S3AFileSystem

class SampleSparkAppS3Spec extends S3Spec with SparkSpec {

  private val hadoopConf = spark.sparkContext.hadoopConfiguration
  hadoopConf.set("fs.s3a.endpoint", AwsEndpointUriStr)
  hadoopConf.set("fs.s3.impl", classOf[S3AFileSystem].getName)
  // keys (access.key, secret.key) can be anything (moto_server doesn't require authentication), but will throw
  // exception if not provided or if provided as "" (empty string)
  hadoopConf.set("fs.s3a.access.key", MockAWSAccessKey)
  hadoopConf.set("fs.s3a.secret.key", MockAWSSecretKey)
  hadoopConf.set("fs.s3a.attempts.maximum", "3")
  hadoopConf.set("fs.s3a.path.style.access", "true")
  hadoopConf.set("fs.s3a.s3.client.factory.impl", classOf[NonChunkedDefaultS3ClientFactory].getName)
  hadoopConf.set("fs.s3a.multiobjectdelete.enable", "false")

  val testSparkApp: SampleSparkAppS3 = new SampleSparkAppS3(spark, testBucketName)

  describe("component reliant on AWS S3 interactions via integration with the Hadoop ecosystem") {
    it("should write data to S3 and read it back via the Spark API") {
      import spark.implicits._
      testSparkApp.writeDfToS3()
      assert(
        mockS3
          .listObjects(testBucketName, SampleSparkAppS3.SparkSqlS3Key)
          .getObjectSummaries
          .size() > 0,
        s"writing to S3 location ${testSparkApp.sparkSqlS3URI} creates S3 objects with key prefix ${SampleSparkAppS3.SparkSqlS3Key}")

      val df = testSparkApp.readDfFromS3()
      assert(
        df.as[Int].collect.toSet === (0 until 500).toSet,
        s"reading from S3 location ${testSparkApp.sparkSqlS3URI} produces DataFrame with data matching data originally written")
    }
  }

}
