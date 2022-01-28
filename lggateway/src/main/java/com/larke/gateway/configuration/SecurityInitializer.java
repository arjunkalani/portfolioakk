package com.larke.gateway.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@ComponentScan(basePackages = "com.larke.gateway")
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

}