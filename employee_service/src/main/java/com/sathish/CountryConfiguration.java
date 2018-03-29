package com.sathish;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "countryEntityManagerFactory",transactionManagerRef = "countryTransactionManager", basePackages = { "com.sathish.country.repo" })
public class CountryConfiguration {

	
	@Bean(name = "countryDataSource")
	@ConfigurationProperties("country.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	
	@Bean(name = "countryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean countryEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("countryDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.sathish.country.model").persistenceUnit("country").build();
	}

	@Bean(name = "countryTransactionManager")
	public PlatformTransactionManager countryTransactionManager(
			@Qualifier("countryEntityManagerFactory") EntityManagerFactory countryEntityManagerFactory) {
		return new JpaTransactionManager(countryEntityManagerFactory);
	}
}
