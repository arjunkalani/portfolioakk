package com.larke.gateway.configuration;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages = "com.larke.gateway")
public class DatabaseConfig {

	@Bean
	public DataSource getDataSourceH2() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.h2.Driver");
		dataSourceBuilder.url("jdbc:h2:mem:watermeterdb");
		dataSourceBuilder.username("sa");
		dataSourceBuilder.password("");
		return dataSourceBuilder.build();
	}

	@Bean
	@Primary
	public DataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/watermeterdb?useSSL=false");
		ds.setUsername("root");
		ds.setPassword("Emecube152018@");
		return ds;
	}

}
