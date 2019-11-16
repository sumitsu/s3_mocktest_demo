package dev.sumitsu.s3mocktest

import java.io.InputStream
import java.nio.charset.StandardCharsets

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import dev.sumitsu.s3mocktest.util.Logging
import org.apache.commons.io.IOUtils

import scala.collection.JavaConverters._
import scala.util.Try

class SampleAppS3(s3: AmazonS3, bucketName: String) extends Logging {

  import SampleAppS3._

  def s3Write(): Unit = {
    ObjData.map { case (s3Key, dataValue) =>
      s3.putObject(bucketName, s3Key, dataValue)
    }
  }

  def s3Read(): Seq[String] = {
    ObjKeys.map { key =>
      val s3ContentStream: InputStream = s3.getObject(bucketName, key).getObjectContent
      val readAttempt: Try[String] = Try(IOUtils.toString(s3ContentStream, StandardCharsets.UTF_8))
      s3ContentStream.close()
      readAttempt.get
    }
  }

  def s3List(): Map[String, String] = {
    s3
      .listObjects(bucketName)
      .getObjectSummaries.asScala
      .map { objSummary => objSummary.getBucketName -> objSummary.getKey }
      .toMap
  }

  def s3Delete(): Unit = {
    s3.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(ObjKeys.toArray:_*))
  }

}

object SampleAppS3 {

  /**
   * Sample data to be written to S3: the first element of each tuple is the S3 key; second element is the value to be
   * written.
   */
  val ObjData: Seq[(String, String)] = Seq(
    "abc" -> "ABC",
    "sub0/def" -> "DEF",
    "sub0/sub1/ghi" -> "GHI"
  )
  val ObjKeys: Seq[String] = ObjData.map(_._1)
  val ObjValues: Seq[String] = ObjData.map(_._2)

}
