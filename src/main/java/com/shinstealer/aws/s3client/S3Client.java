package com.shinstealer.aws.s3client;

import java.time.Instant;

public interface S3Client {

	String HTTP_PROXY = "HTTP_PROXY";
	
	static S3Client createProfileCredentialS3Client(String profileName) {
		return new ProfileCredentialsS3Client(profileName);
	}
	
	byte[] getObject(String bucket , String key);
	
	String putObject(String bucket , String key , byte[] body);
	
	String putObject(String bucket , String key , byte[] body , Instant expired);
	
	HeadResponse headObject(String bucket , String key);
	
	String accessBucketHead(String bucket);
}
