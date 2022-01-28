package com.larke.gateway.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@ComponentScan(basePackages = "com.larke.gateway")
@Configuration
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	static final Logger LOGGER = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {
		if (authentication != null && authentication.getDetails() != null) {
			try {
				httpServletRequest.getSession().invalidate();
				LOGGER.info("User Successfully Logout");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		httpServletResponse.sendRedirect("/loginPage");
	}

}
