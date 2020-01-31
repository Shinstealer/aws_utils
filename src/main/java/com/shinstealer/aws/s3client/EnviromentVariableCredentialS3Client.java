package com.shinstealer.aws.s3client;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;

public class EnviromentVariableCredentialS3Client extends AbstractS3Client {

	public EnviromentVariableCredentialS3Client() {
		s3 = software.amazon.awssdk.services.s3.S3Client.builder().region(Region.AP_NORTHEAST_1)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();

	}
}
