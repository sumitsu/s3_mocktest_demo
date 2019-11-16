package dev.sumitsu.s3mocktest.testutil

import java.util.UUID

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

object AmazonS3TestUtil {

  val MockS3ServerPortEnvVar: String = "MOTO_SERVER_PORT"
  val MockS3ServerPortDefault: Int = 9999
  val AwsEndpointUriStr: String =
    s"http://localhost:${Option(System.getenv(MockS3ServerPortEnvVar)).getOrElse(MockS3ServerPortDefault)}/"
  val TestBucketName: String =
    s"s3-mocktest-demo-${UUID.randomUUID.toString}"
  val MockAWSAccessKey: String = "abc"
  val MockAWSSecretKey: String = "zyx"

  def buildLocalMockTestS3Client(): AmazonS3 = {
    AmazonS3ClientBuilder
      .standard
      .withPathStyleAccessEnabled(true)
      .disableChunkedEncoding
      .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
        AwsEndpointUriStr, Regions.US_EAST_1.getName))
      .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(MockAWSAccessKey, MockAWSSecretKey)))
      .build()
  }
}
