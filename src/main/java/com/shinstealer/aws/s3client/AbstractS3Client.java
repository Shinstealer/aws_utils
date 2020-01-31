package com.shinstealer.aws.s3client;

import java.time.Instant;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public abstract class AbstractS3Client implements S3Client {

	protected software.amazon.awssdk.services.s3.S3Client s3;

	@Override
	public byte[] getObject(String bucket, String key) {
		return s3.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build(), ResponseTransformer.toBytes())
				.asByteArray();
	}

	@Override
	public String putObject(String bucket, String key, byte[] body) {
		return s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromBytes(body))
				.eTag();
	}

	@Override
	public String putObject(String bucket, String key, byte[] body, Instant expired) {
		return s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).expires(expired).build(),
				RequestBody.fromBytes(body)).eTag();
	}

	@Override
	public HeadResponse headObject(String bucket, String key) {
		return new HeadResponse(s3.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build()));
	}

	@Override
	public String accessBucketHead(String bucket) {
		return s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build()).responseMetadata().requestId();
	}

}
