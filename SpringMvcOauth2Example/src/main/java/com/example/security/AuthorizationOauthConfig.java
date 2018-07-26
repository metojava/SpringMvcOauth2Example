package com.example.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.example.utils.CustomTokenEnhancer;

@EnableAuthorizationServer
@Configuration
public class AuthorizationOauthConfig extends
		AuthorizationServerConfigurerAdapter {

	// @Autowired
	// DataSource dataSource;
	@Bean
	TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/test");
		driverManagerDataSource.setUsername("root");
		driverManagerDataSource.setPassword("nbuser");
		return driverManagerDataSource;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource());
	}

	@Bean
	JwtTokenStore jwtTokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
				new ClassPathResource("keystore.jks"),
				"storepass".toCharArray());
		tokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("testkey"));
		return tokenConverter;
	}

	@Bean
	@Primary
	DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(jwtTokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		defaultTokenServices.setTokenEnhancer(accessTokenConverter());
		return defaultTokenServices;
	}

	// @Autowired
	// @Qualifier("userDetailsService")
	// private UserDetailsService userDetailsService;

	@Autowired
	UserApprovalHandler userApprovalHandler;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		super.configure(endpoints);
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));

		endpoints.tokenStore(tokenStore())
				.tokenServices(tokenServices())
				.tokenEnhancer(enhancerChain)
				.userApprovalHandler(userApprovalHandler)
				.authenticationManager(authenticationManager)
				.accessTokenConverter(accessTokenConverter())
				// .userDetailsService(userDetailsService)
				.allowedTokenEndpointRequestMethods(HttpMethod.GET,
						HttpMethod.POST);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer)
			throws Exception {
		oauthServer.tokenKeyAccess(
				"isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
				.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		super.configure(clients);
		clients.jdbc(dataSource())
				.inMemory()
				.withClient("acme")
				.secret("acmesecret")
				.authorities("ROLE_ADMIN", "ROLE_TRUSTED_CLIENT")
				.authorizedGrantTypes("authorization_code", "refresh_token",
						"password").scopes("openid", "read", "write", "trust")
				.accessTokenValiditySeconds(480)
				.refreshTokenValiditySeconds(600).autoApprove(true);
	}

}
