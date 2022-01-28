package com.larke.gateway;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.larke.gateway")
public class WebGatewayApplication {

	static final Logger log = LoggerFactory.getLogger(WebGatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebGatewayApplication.class, args);

		log.info("Before Starting application");
		SpringApplication.run(WebGatewayApplication.class, args);
		log.debug("Starting my application in debug with {} args", args.length);
		log.info("Starting my application with {} args.", args.length);
	}

}
