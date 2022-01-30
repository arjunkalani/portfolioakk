package com.larke.gateway.web;


import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.larke.gateway.model.User;
import com.larke.gateway.service.UserService;


@Controller
@ComponentScan(basePackages = "com.larke.gateway")
public class PortfolioController {

	static final Logger LOGGER = LoggerFactory.getLogger(PortfolioController.class);

	@Autowired
	private UserService userService;

	@GetMapping(value = { "/kuLink" })
	public String userManagement(Model model) {
		return "https://kalaniuniversity.azurewebsites.net";
	}

	@ModelAttribute("userName")
	private String getUserName(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		return user.getFirstname() + " " + user.getLastname() + "!" + " (" + user.getEmail() + ") ";
	}
	
	@ModelAttribute("roles")
	private Collection<? extends GrantedAuthority> getRoles(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities();
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	public String userInfo(Authentication authentication) {
		String userName = authentication.getName();
		String role = authentication.getAuthorities().stream().findAny().get().getAuthority();
		return "Your user name is: " + userName + " and your role is: " + role;
	}

	@ModelAttribute("user")
	public User user() {
		return new User();
	}

}
