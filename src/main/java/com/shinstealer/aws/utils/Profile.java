package com.shinstealer.aws.utils;

import java.util.Optional;

public class Profile {

	private software.amazon.awssdk.profiles.Profile profile;

	/**
	 * @param awssdk profile
	 *
	 */
	public Profile(software.amazon.awssdk.profiles.Profile profile) {
		this.profile = profile;
	}

	/**
	 * @param property
	 * @return value of property || empty
	 */
	public Optional<String> getProperty(String property) {
		return profile.property(property);
	}
}
