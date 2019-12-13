package dev.sumitsu.s3mocktest.testutil;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.S3ClientOptions;
import org.apache.hadoop.fs.s3a.DefaultS3ClientFactory;

import java.io.IOException;
import java.net.URI;

public class NonChunkedDefaultS3ClientFactory extends DefaultS3ClientFactory {
  @Override
  public AmazonS3 createS3Client(final URI name, final String bucket, final AWSCredentialsProvider credentials) throws IOException {
    final AmazonS3 s3 = super.createS3Client(name, bucket, credentials);
    s3.setS3ClientOptions(
      S3ClientOptions.builder()
        .disableChunkedEncoding()
        .setPathStyleAccess(true)
        .build());
    return s3;
  }
}