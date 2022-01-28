package com.larke.gateway.mapper;

import java.sql.ResultSet;

import java.sql.SQLException;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.RowMapper;

import com.larke.gateway.model.Role;

@ComponentScan(basePackages = "com.larke.gateway")
public class RoleMapper implements RowMapper<Role> {

	public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
		Role role = new Role();
		role.setId(rs.getLong("id"));
		role.setRole(rs.getString("role"));
		return role;
	}

}
