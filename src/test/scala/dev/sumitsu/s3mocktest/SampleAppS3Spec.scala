package dev.sumitsu.s3mocktest

import java.util.UUID

import com.amazonaws.services.s3.AmazonS3
import dev.sumitsu.s3mocktest.testutil.AmazonS3TestUtil
import dev.sumitsu.s3mocktest.util.Logging
import org.scalatest.{BeforeAndAfterAll, FunSpec}

class SampleAppS3Spec extends FunSpec with BeforeAndAfterAll with Logging {

  val testBucketName: String = UUID.randomUUID.toString
  val mockS3: AmazonS3 = AmazonS3TestUtil.buildLocalMockTestS3Client()
  val testApp: SampleAppS3 = new SampleAppS3(mockS3, testBucketName)

  override def beforeAll(): Unit = {
    super.beforeAll()
    mockS3.createBucket(testBucketName)
  }

  describe("component reliant on AWS S3 interactions") {
    it("should write objects to S3 and read them back") {
      testApp.s3Write()
      assert(
        testApp.s3List() === SampleAppS3.ObjKeys.map(s3Key => testBucketName -> s3Key).toMap,
        "list of objects in S3 should have bucket and key/path matching original write")
      assert(
        testApp.s3Read() === SampleAppS3.ObjValues,
        "values read back from S3 should match those written")
    }
    it("should delete objects from S3") {
      testApp.s3Delete()
      assert(
        testApp.s3List() === Map.empty,
        "list of objects in S3 should be empty following delete")
    }
  }

}
