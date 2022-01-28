package com.larke.gateway.web;


import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.larke.gateway.repository.RoleRepository;
import com.larke.gateway.repository.UserRepository;
import com.larke.gateway.service.RoleService;
import com.larke.gateway.service.UserService;
import com.larke.gateway.model.Role;
import com.larke.gateway.model.User;

@Controller
@ComponentScan(basePackages = "com.larke.gateway")
public class GreetingController {

	static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@GetMapping(path = { "/index" })
	public String setRoot(Model model) {
		model.addAttribute("pageTitle", "Welcome to the secure gateway - v01.28.22");
		return "static/index";
	}

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "Guest") String name,
			Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	@GetMapping(path = { "/loginPage" })
	public String loginPage(Model model) {
		return "static/loginPage";
	}

	@GetMapping(path = { "/logout" })
	public String logoutPage(Model model) {
		return "static/loginPage?logout=true";
	}

	@PostMapping(path = { "/login_in_process" })
	public String loginInProcess(Model model) {
		LOGGER.info("REPORTING FROM login_in_process: ");
		return "redirect:secureWelcome";
	}

	@GetMapping(path = { "/", "/secureWelcome" })
	public String secureDashboard(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("roles", auth.getAuthorities());
		LOGGER.info("REPORTING FROM secureWelcome: ");
		return "dashboard/secureWelcome";
	}

	@GetMapping(path = "/esignup")
	public String emailSignup(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		List<Role> role = new ArrayList<>();
		model.addAttribute("role", role);
		List<Role> roles = roleRepository.findAll();
		model.addAttribute("roles", roles);
		return "static/esignup";
	}

	@GetMapping(path = "/access_denied")
	public String accessDenied(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("roles", auth.getAuthorities());
		LOGGER.info("REPORTING FROM /access_denied: ");
		return "error/access_denied";
	}

	@ModelAttribute("userName")
	private String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		return user.getFirstname() + " " + user.getLastname() + "!" + " (" + user.getEmail() + ") ";
	}

}
