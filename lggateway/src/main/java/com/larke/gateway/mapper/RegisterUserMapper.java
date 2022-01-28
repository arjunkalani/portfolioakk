package com.larke.gateway.mapper;

import java.sql.ResultSet;

import java.sql.SQLException;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.RowMapper;

import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
public class RegisterUserMapper implements RowMapper<User> {
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setFirstname(rs.getString("firstname"));
		user.setLastname(rs.getString("lastname"));
		return user;
	}
}
