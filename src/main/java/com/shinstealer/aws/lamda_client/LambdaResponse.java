package com.shinstealer.aws.lamda_client;

public class LambdaResponse {

	Integer statusCode;

	byte[] payload;

	public LambdaResponse(Integer statusCode, byte[] payload) {

		this.statusCode = statusCode;
		this.payload = payload;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public byte[] getPayload() {
		return payload;
	}

}
