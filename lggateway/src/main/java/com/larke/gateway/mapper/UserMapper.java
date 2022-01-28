package com.larke.gateway.mapper;

import java.sql.ResultSet;

import java.sql.SQLException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.RowMapper;
import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
public class UserMapper implements RowMapper<User> {

	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setFirstname(rs.getString("firstname"));
		user.setLastname(rs.getString("lastname"));
		user.setActive(rs.getInt("active"));
		user.setEnabled(rs.getBoolean("enabled"));
		user.setAccountexpired(rs.getBoolean("accountexpired"));
		user.setCredentialsexpired(rs.getBoolean("credentialsexpired"));
		user.setAccountlocked(rs.getBoolean("accountlocked"));
		user.setCreatedtime(rs.getString("createdtime"));
		user.setUpdatedtime(rs.getString("updatedtime"));
		user.setConfirmationtoken(rs.getString("confirmationtoken"));
		user.setVerificationCode(rs.getString("verificationcode"));
		return user;
	}
}
