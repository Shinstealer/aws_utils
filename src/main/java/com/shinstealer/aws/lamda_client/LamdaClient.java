package com.shinstealer.aws.lamda_client;

public interface LamdaClient {

	String HTTP_PROXY = "HTTP_PROXY";
	
	LambdaResponse invokeUTF8(String functionName , String utf8String);
	
	LambdaResponse invoke(String funtionName , byte[] payload);
	
	static LamdaClient createProfileCredentialsLambdaClient(String profileName) {
		return new ProfileCredentialsLambdaClient(profileName);
	}
	
}
