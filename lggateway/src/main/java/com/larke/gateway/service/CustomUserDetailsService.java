package com.larke.gateway.service;

import java.util.Collection;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.larke.gateway.dao.LoginDao;
import com.larke.gateway.model.Role;
import com.larke.gateway.model.User;
import com.larke.gateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("UserDetailsService")
@ComponentScan(basePackages = "com.larke.gateway")
public class CustomUserDetailsService implements UserDetailsService {

	static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoginDao loginDao;

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		LOGGER.info(
				"from Collection = " + user.getEmail() + user.getPassword() + mapRolesToAuthorities(user.getRoles()));
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user with that email");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

}
