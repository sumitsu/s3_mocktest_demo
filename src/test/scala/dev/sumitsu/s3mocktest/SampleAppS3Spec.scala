package dev.sumitsu.s3mocktest

import dev.sumitsu.s3mocktest.testutil.S3Spec

class SampleAppS3Spec extends S3Spec {

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
