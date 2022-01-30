package com.larke.gateway.web;


import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.larke.gateway.model.Role;
import com.larke.gateway.model.User;
import com.larke.gateway.repository.UserRepository;
import com.larke.gateway.service.EmailService;
import com.larke.gateway.service.RoleService;
import com.larke.gateway.service.UserService;


@Controller
@ComponentScan(basePackages = "com.larke.gateway")
public class UserController {

	static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping(path = "/process_register")
	public ModelAndView processRegistrationForm(@ModelAttribute("user") @Valid User user, String roles,
			BindingResult bindingResult, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		User userExists = userService.findByEmail(user.getEmail());
		LOGGER.info("REPORTING FROM /process_register userExists: ", userExists, bindingResult);
		LOGGER.info("REPORTING FROM /process_register roles ", roles);
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "This email already exists!");
		}
		if (bindingResult.hasErrors()) {
			model.setViewName("static/esignup");
		} else {
			emailService.register(user, getSiteURL(request), roles);
			LOGGER.info("REPORTING FROM /process_register getSiteURL(request): ", getSiteURL(request));
			model.setViewName("static/registerSuccess");
		}
		return model;
	}

	@GetMapping(path = "/verify")
	public String verifyUser(@Param("code") String code) {
		if (emailService.verify(code)) {
			return "dashboard/admin/users/verifySuccess";
		} else {
			return "dashboard/admin/users/verifyFailed";
		}
	}

	@GetMapping(path = "/adminDashboard")
	public String adminDashboard(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("roles", auth.getAuthorities());
		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			return "dashboard/admin/adminDashboard";
		} else {
			return "redirect:access_denied";
		}
	}

	@GetMapping(value = { "/userDashboard" })
	public String userDashboard(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("roles", auth.getAuthorities());
		if ((auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))
				|| auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))
				|| auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EDITOR"))
				|| auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("DBA")))) {
			return "dashboard/user/userDashboard";
		} else {
			return "redirect:access_denied";
		}

	}

	@GetMapping(value = { "/userManagement" })
	public String userManagement(Model model) {
		return "dashboard/admin/users/userManagement";
	}

	@GetMapping(value = { "/userList" })
	public ModelAndView userListInFiles() {
		ModelAndView model = new ModelAndView();
		model.addObject("userList", userRepository.findAll());
		model.setViewName("dashboard/admin/users/userList");
		return model;
	}

	@GetMapping(path = "/userListRole")
	public ModelAndView userListRole(User userl) {
		ModelAndView model = new ModelAndView();
		model.addObject("userListRole", userService.listAll());
		model.setViewName("dashboard/admin/users/userListRole");
		return model;
	}

	@GetMapping(path = "/updateInputRadio")
	public ModelAndView updateInputRadio() {
		ModelAndView model = new ModelAndView("updateInputRadio");
		model.setViewName("dashboard/admin/users/userEdit");
		return model;
	}

	@GetMapping(path = "/view/{id}")
	public ModelAndView view(@PathVariable("id") long id) {
		ModelAndView model = new ModelAndView("userView");
		User user = userService.getSingleUserById(id);
		List<Role> listRoles = roleService.listAll();
		model.addObject("user", user);
		model.addObject("listRoles", listRoles);
		model.setViewName("dashboard/admin/users/userView");
		return model;
	}

	@GetMapping(path = "/fullEdit/{id}")
	public ModelAndView fullEdit(@PathVariable("id") long id) {
		ModelAndView model = new ModelAndView("userEdit");
		User user = userService.getSingleUserById(id);
		model.addObject("user", user);
		model.setViewName("dashboard/admin/users/userEdit");
		return model;
	}

	@GetMapping(path = "/edit/{id}")
	public ModelAndView edit(@PathVariable("id") long id) {
		ModelAndView model = new ModelAndView("userForm");
		User user = userService.getSingleUserById(id);
		List<Role> listRoles = roleService.listAll();
		model.addObject("user", user);
		model.addObject("userForm", listRoles);
		model.setViewName("dashboard/admin/users/userForm");
		return model;
	}

	@PostMapping(path = "/updateUser")
	public ModelAndView updateUser(User user, BindingResult bindingResult, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		user.setId(user.getId());
		userService.updateAUserById(user);
		model.addObject("msg", "User has been updated successfully!");
		model.addObject("user", user);
		model.setViewName("redirect:userListRole");
		return model;
	}

	@GetMapping(path = "/delete/{id}")
	public ModelAndView viewUserFordeletion(@PathVariable("id") long id) {
		ModelAndView model = new ModelAndView();
		User user = userService.getSingleUserById(id);
		model.addObject("user", user);
		model.setViewName("dashboard/admin/users/userDelete");
		return model;
	}

	@PostMapping(path = "/userDelete")
	public ModelAndView deleteAUserRecord(long id) {
		ModelAndView model = new ModelAndView("userDelete");
		userService.deleteById(id);
		model.setViewName("redirect:userListRole");
		return model;
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
