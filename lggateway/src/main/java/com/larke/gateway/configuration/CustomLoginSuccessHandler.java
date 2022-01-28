package com.larke.gateway.configuration;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@ComponentScan(basePackages = "com.larke.gateway")
@Configuration
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	static final Logger LOGGER = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("USER")) {
				redirectStrategy.sendRedirect(request, response, "/userDashboard");
			}
			if (grantedAuthority.getAuthority().equals("ADMIN")) {
				redirectStrategy.sendRedirect(request, response, "/adminDashboard");
			}
		}
	}
}
