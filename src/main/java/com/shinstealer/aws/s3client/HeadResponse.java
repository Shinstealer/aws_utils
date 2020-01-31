package com.shinstealer.aws.s3client;

import java.time.Instant;

import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

public class HeadResponse {

	private HeadObjectResponse response;

	public HeadResponse(HeadObjectResponse response) {
		this.response = response;
	}

	public Instant lastModified() {
		return response.lastModified();
	}
}
