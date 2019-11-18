package dev.sumitsu.s3mocktest.testutil

import java.util.UUID

import com.amazonaws.services.s3.AmazonS3
import dev.sumitsu.s3mocktest.util.Logging
import org.scalatest.{BeforeAndAfterAll, FunSpec}

class S3Spec extends FunSpec with BeforeAndAfterAll with Logging {

  val testBucketName: String = UUID.randomUUID.toString
  val mockS3: AmazonS3 = AmazonS3TestUtil.buildLocalMockTestS3Client()

}
