package com.example.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.example.*", "com.tv.*" })
public class WebConfig extends WebMvcConfigurerAdapter {

	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	/*
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * super.addCorsMappings(registry); registry.addMapping("/**")
	 * .allowedOrigins("*") .allowCredentials(false) .allowedHeaders(
	 * "Access-Control-Allow-Headers",
	 * "Accept, Content-Type, Authorization, Origin, " +
	 * "X-Requested-With, X-Auth-Token, Access-Control-Request-Method," +
	 * " Access-Control-Request-Headers") .allowedMethods("POST", "GET", "PUT",
	 * "OPTIONS", "DELETE") .maxAge(3600); }
	 */

}
