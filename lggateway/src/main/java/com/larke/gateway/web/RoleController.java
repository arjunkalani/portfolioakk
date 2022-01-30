package com.larke.gateway.web;


import java.util.Collection;

import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.larke.gateway.dao.RoleDao;
import com.larke.gateway.model.Role;
import com.larke.gateway.model.User;
import com.larke.gateway.repository.RoleRepository;
import com.larke.gateway.service.RoleService;
import com.larke.gateway.service.UserService;
import com.larke.gateway.validator.RoleValidator;

@Controller
@ComponentScan(basePackages = "com.larke.gateway")
public class RoleController {

	static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	RoleService roleService;

	@Autowired
	UserService userService;

	@Autowired
	RoleDao roleDao;

	@Autowired
	private RoleValidator roleValidator;

	private static final String ROLELIST = "roleList";
	private static final String REDIRECT = "redirect:/roleList";

	@RequestMapping(path = "/roleList")
	public ModelAndView listAllRole() {
		ModelAndView model = new ModelAndView();
		model.addObject(ROLELIST, roleService.findAllRoles());
		model.setViewName("dashboard/admin/roles/roleList");
		return model;
	}

	@GetMapping(path = "/roleAdd")
	public String roleAdd(Model model) {
		model.addAttribute("roleAdd", new Role());
		LOGGER.info("REPORTING FROM /roleAdd: ");
		return "dashboard/admin/roles/roleAdd";
	}

	@PostMapping(path = "/roleAdd")
	public String createRole(@Valid @ModelAttribute("roleAdd") Role roleAdd, BindingResult bindingResult,
			RedirectAttributes redirectAttrs) {
		roleValidator.validate(roleAdd, bindingResult);
		if (bindingResult.hasErrors()) {
			return "dashboard/admin/roles/roleAdd";
		}
		roleService.saveRole(roleAdd);
		redirectAttrs.addAttribute(ROLELIST, roleAdd.getRole()).addFlashAttribute("message", "Role created!");
		return REDIRECT;
	}

	@GetMapping(path = "/roleView/{id}")
	public ModelAndView roleView(@PathVariable("id") long id) {
		ModelAndView model = new ModelAndView("roleView");
		Role role = roleService.getARecordByRoleId(id);
		model.addObject("roleView", role);
		model.setViewName("dashboard/admin/roles/roleView");
		return model;
	}

	@GetMapping(path = "/roleEdit/{id}")
	public ModelAndView editRoleById(@PathVariable("id") long id) {
		ModelAndView model = new ModelAndView("roleForm");
		Role role = roleService.getARecordByRoleId(id);
		model.addObject("roleForm", role);
		model.setViewName("dashboard/admin/roles/roleForm");
		return model;

	}

	@PostMapping(path = "/roleUpdate")
	public ModelAndView roleUpdateById(long id, String role) {
		roleService.updateARoleById(id, role);
		ModelAndView model = new ModelAndView(ROLELIST);
		model.addObject("msg", "Role has been updated successfully!");
		return new ModelAndView(REDIRECT);
	}

	@GetMapping(path = "/roleDelete/{id}")
	public ModelAndView verifyRoleById(@PathVariable("id") long id) {
		ModelAndView model = new ModelAndView();
		Role role = roleService.getARecordByRoleId(id);
		model.addObject("roleDelete", role);
		model.setViewName("dashboard/admin/roles/roleDelete");
		LOGGER.info("REPORTING FROM /roleDelete/{id}: ");
		return model;
	}

	@PostMapping(path = "/roleDelete")
	public String deleteARole(long id) {
		roleService.deleteRoleById(id);
		LOGGER.info("REPORTING FROM /roleDelete: ");
		return REDIRECT;
	}

	@GetMapping(path = "/roleManagement")
	public ModelAndView userManagement() {
		ModelAndView model = new ModelAndView();
		model.setViewName("dashboard/admin/roles/roleManagement");
		return model;
	}

	@ModelAttribute("userName")
	private String getUserName() {
		String userName;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		userName = user.getFirstname() + " " + user.getLastname();
		return userName;
	}
	
	@ModelAttribute("roles")
	private Collection<? extends GrantedAuthority> getRoles(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities();
	}

}
