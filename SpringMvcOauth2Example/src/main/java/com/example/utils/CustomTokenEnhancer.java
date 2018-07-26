package com.example.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
			OAuth2Authentication authentication) {
		Map<String, Object> additionalInformation = new HashMap<>();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		additionalInformation.put("authorities", userDetails.getAuthorities());
		((DefaultOAuth2AccessToken) accessToken)
				.setAdditionalInformation(additionalInformation);
		return accessToken;
	}

}
