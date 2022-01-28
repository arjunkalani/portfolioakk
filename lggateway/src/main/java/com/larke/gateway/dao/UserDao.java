package com.larke.gateway.dao;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;

import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
public interface UserDao {

	public List<User> listAllUsersDAO();

	public void updateAUserById(User user);

	public boolean userExists(String email);

	public void add(String email);

	public User getSingleUserById(Long id);

	public void registerAUserByEmail(String email);

	public List<User> listUsersWithRoles();

	public User getUserById(long id);

	public User findUserByEmail(String email);

	public List list();

	public void deleteAUserRecordById(long id);

}
