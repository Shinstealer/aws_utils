package com.shinstealer.aws.lamda_client;

import java.util.Objects;

import com.shinstealer.aws.utils.AwsUtil;

import software.amazon.awssdk.services.lambda.LambdaClientBuilder;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.apache.ApacheHttpClient.Builder;

public class ProfileCredentialsLambdaClient implements LamdaClient {

	private software.amazon.awssdk.services.lambda.LambdaClient lambda;

	public ProfileCredentialsLambdaClient(String profileName) {

		LambdaClientBuilder clientBuilder = software.amazon.awssdk.services.lambda.LambdaClient.builder()
				.region(AwsUtil.getDefaultRegion())
				.credentialsProvider(AwsUtil.getProfileCredentialsProvider(profileName));

		Builder httpProxyBuilder = AwsUtil.getHttpProxyBuilder(HTTP_PROXY);

		if (Objects.nonNull(httpProxyBuilder)) {
			clientBuilder.httpClientBuilder(httpProxyBuilder);
		}

		lambda = clientBuilder.build();
	}

	@Override
	public LambdaResponse invokeUTF8(String functionName, String utf8String) {
		InvokeResponse response = lambda.invoke(InvokeRequest.builder().functionName(functionName)
				.payload(SdkBytes.fromUtf8String(utf8String)).build());
		return createLambdaResponse(response);
	}

	@Override
	public LambdaResponse invoke(String functionName, byte[] payload) {
		InvokeResponse response = lambda.invoke(
				InvokeRequest.builder().functionName(functionName).payload(SdkBytes.fromByteArray(payload)).build());
		return createLambdaResponse(response);
	}

	private LambdaResponse createLambdaResponse(InvokeResponse response) {

		LambdaResponse res = new LambdaResponse(response.statusCode(), response.payload().asByteArray());
		return res;
	}
}
