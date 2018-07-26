package com.example.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@ComponentScan("com.example.*")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Autowired
	ClientDetailsService clientDetailsService;

	// @Autowired
	// public void configAuthentication(AuthenticationManagerBuilder auth)
	// throws Exception {
	// auth.jdbcAuthentication()
	// .dataSource(dataSource)
	// .usersByUsernameQuery(
	// "select username,password, enabled from users where username=?")
	// .authoritiesByUsernameQuery(
	// "select username, role from user_roles where username=?");
	// }

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication().withUser("mamuka").password("arabuli")
				.roles("ADMIN").and().withUser("ananda").password("bollu")
				.roles("MANAGER").and().withUser("charlie")
				.password("uiprogrammer").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().anonymous().disable().authorizeRequests()
				.antMatchers("/oauth/token").permitAll().antMatchers("/js")
				.permitAll().antMatchers("/admin/**")
				.access("hasRole('ROLE_ADMIN')").antMatchers("/rest/**")
				.access("hasRole('ROLE_ADMIN')").and().formLogin()
				.loginPage("/login").failureUrl("/login?error")
				.usernameParameter("username").passwordParameter("password")
				.and().logout().logoutSuccessUrl("/login?logout").and()
				.exceptionHandling().accessDeniedPage("/403").and().csrf();

	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	public ApprovalStore approvalStore() {
		TokenApprovalStore approvalStore = new TokenApprovalStore();
		approvalStore.setTokenStore(tokenStore());
		return approvalStore;
	}

	@Bean
	public TokenStoreUserApprovalHandler userApprovalHandler() {
		TokenStoreUserApprovalHandler userApprovalHandler = new TokenStoreUserApprovalHandler();
		userApprovalHandler.setTokenStore(tokenStore());
		userApprovalHandler.setRequestFactory(new DefaultOAuth2RequestFactory(
				clientDetailsService));
		userApprovalHandler.setClientDetailsService(clientDetailsService);
		return userApprovalHandler;

	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedHeaders(Arrays.asList(
				"Access-Control-Allow-Headers", "Accept", "Content-Type",
				"Authorization", "Origin", "X-Requested-With, X-Auth-Token",
				"Access-Control-Request-Method",
				"Access-Control-Request-Headers"));
		corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT",
				"OPTIONS", "DELETE"));
		corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;

	}
}