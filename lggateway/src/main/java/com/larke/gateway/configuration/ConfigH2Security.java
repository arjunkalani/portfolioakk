package com.larke.gateway.configuration;

import org.springframework.context.annotation.ComponentScan;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(value = 0)
@ComponentScan(basePackages = "com.larke.gateway")
public class ConfigH2Security extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/h2-console/**").authorizeRequests().anyRequest().permitAll();
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
}
