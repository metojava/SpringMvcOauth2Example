package com.example.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	DefaultTokenServices tokenServices;

	@Autowired
	JwtTokenStore jwtTokenStore;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources)
			throws Exception {
		super.configure(resources);
		resources.resourceId("my_rest_app").stateless(false)
				.tokenStore(jwtTokenStore).tokenServices(tokenServices);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS).permitAll().and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
				.anonymous().disable().authorizeRequests()
				.antMatchers("/rest/**").access("hasRole('ADMIN')").and()
				.exceptionHandling()
				.accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}

	private class CustomRequestMatcher implements RequestMatcher {

		@Override
		public boolean matches(HttpServletRequest request) {
			String auth = request.getHeader("Authorization");
			boolean has_access_token = auth.contains("access_token");
			if ((auth != null) && auth.contains("Bearer") && !has_access_token)
				has_access_token = true;
			return has_access_token;
		}

	}
}
