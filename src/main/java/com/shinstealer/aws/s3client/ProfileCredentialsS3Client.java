package com.shinstealer.aws.s3client;

import java.util.Objects;

import com.shinstealer.aws.utils.AwsUtil;

import software.amazon.awssdk.http.apache.ApacheHttpClient.Builder;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

public class ProfileCredentialsS3Client extends AbstractS3Client {

	public ProfileCredentialsS3Client(String profileName) {
		S3ClientBuilder clientBuilder = software.amazon.awssdk.services.s3.S3Client.builder()
				.region(AwsUtil.getDefaultRegion())
				.credentialsProvider(AwsUtil.getProfileCredentialsProvider(profileName));
		Builder httpProxyBuilder = AwsUtil.getHttpProxyBuilder(HTTP_PROXY);

		if (Objects.nonNull(httpProxyBuilder)) {
			clientBuilder.httpClientBuilder(httpProxyBuilder);
		}

		s3 = clientBuilder.build();
	}
}
