package com.shinstealer.aws.utils;

import java.net.URI;
import java.util.Objects;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.http.apache.ApacheHttpClient.Builder;
import software.amazon.awssdk.profiles.ProfileFile;
import software.amazon.awssdk.regions.Region;

/**
 * @author kim
 *
 */
public class AwsUtil {
	/**
	 * @set default region
	 *
	 */
	public static Region getDefaultRegion() {
		return Region.AP_NORTHEAST_1;
	}

	/**
	 * @param profile name
	 * @return credential provider with profile name
	 */
	public static AwsCredentialsProvider getProfileCredentialsProvider(String profileName) {
		return ProfileCredentialsProvider.builder().profileName(profileName).build();
	}

	/**
	 * @param profile name
	 * @return profile info
	 */
	public static Profile getProfile(String profileName) {
		return new Profile(ProfileFile.defaultProfileFile().profile(profileName).get());
	}

	public static Builder getHttpProxyBuilder(String envVariableName) {
		String proxy = System.getenv(envVariableName);

		if (Objects.nonNull(proxy)) {
			return ApacheHttpClient.builder()
					.proxyConfiguration(ProxyConfiguration.builder().endpoint(URI.create(proxy)).build());

		}
		return null;
	}
}
