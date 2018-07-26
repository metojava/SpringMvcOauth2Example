package com.example.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.tv.pageablerepos" })
@ComponentScan(basePackages = { "com.tv.*" })
public class DaoConfig {

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		driverManagerDataSource
				.setUrl("jdbc:mysql://localhost:3306/television");
		driverManagerDataSource.setUsername("root");
		driverManagerDataSource.setPassword("nbuser");
		return driverManagerDataSource;
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setPackagesToScan(new String[] { "com.tv.model" });
		// emf.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		JpaVendorAdapter adaptor = new HibernateJpaVendorAdapter();
		emf.setJpaVendorAdapter(adaptor);
		emf.setJpaProperties(jpaProperties());

		return emf;
	}

	@Bean
	PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;

	}

	private Properties jpaProperties() {
		Properties props = new Properties();
		props.setProperty("hibernate.dialect",
				"org.hibernate.dialect.MySQL5Dialect");
		props.setProperty("hibernate.max_fetch_depth", "3");
		props.setProperty("hibernate.jdbc.fetch_size", "25");
		props.setProperty("hibernate.jdbc.batch_size", "5");
		props.setProperty("hibernate.show_sql", "true");
		return props;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

}
