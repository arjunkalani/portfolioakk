package com.larke.gateway.model;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;

@ComponentScan(basePackages = "com.larke.gateway")
public abstract class CustomUserDetails extends User implements UserDetails {

	User user;

	
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	@Override
	public String getFullName() {
		return super.getFirstname() + " " + super.getLastname();
	}

}
