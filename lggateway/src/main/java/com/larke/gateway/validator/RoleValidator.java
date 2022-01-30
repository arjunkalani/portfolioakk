package com.larke.gateway.validator;

import com.larke.gateway.model.Role;
import com.larke.gateway.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@ComponentScan(basePackages = "com.larke.gateway")
public class RoleValidator implements Validator {

	@Autowired
	private RoleService roleService;

	@Override
	public boolean supports(Class<?> aClass) {
		return Role.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Role role = (Role) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "NotEmpty");
		if (role.getRole().length() < 2 || role.getRole().length() > 32) {
			errors.rejectValue("role", "Size.role.role");
		}
		if (roleService.findByRole(role.getRole()) != null) {
			errors.rejectValue("role", "Duplicate.role.role");
		}
		if (role.getRole().equals("ADMIN")) {
			errors.rejectValue("role", "Admin.role.role");
		}
	}
}
