package com.larke.gateway.service;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
public interface UserService {

	public void create(User user);

	void save(User user);

	void update(User user);

	public User findByEmail(String email);

	public List<User> listAllUsersDAO();

	public User getSingleUserById(long id);

	public List<User> listUsersWithRoles();

	public List<User> listAll();

	public void updateAUserById(User user);

	public User getUserById(long id);

	public User findUserByEmail(String email);

	public void deleteAUserRecordById(long id);

	public void deleteById(long id);

}
