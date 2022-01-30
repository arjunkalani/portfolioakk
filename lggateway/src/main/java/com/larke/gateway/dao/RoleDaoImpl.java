package com.larke.gateway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import com.larke.gateway.mapper.RoleMapper;
import com.larke.gateway.model.Role;

@ComponentScan(basePackages = "com.larke.gateway")
@Repository
public class RoleDaoImpl implements RoleDao {

	static final Logger LOGGER = LoggerFactory.getLogger(RoleDaoImpl.class);

	private JdbcTemplate jdbcTemp;

	public RoleDaoImpl(DataSource dataSource) {
		jdbcTemp = new JdbcTemplate(dataSource);
	}

	public List<Role> findAllRoles() {
		String selectQuery = "select * from role";
		return this.jdbcTemp.query(selectQuery, new RoleIdMapper());
	}

	private static final class RoleIdMapper implements RowMapper<Role> {
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setId(rs.getLong("id"));
			role.setRole(rs.getString("role"));
			return role;
		}
	}

	public Role getARecordByRoleId(long id) {
		String selectQuery = "select * FROM role where id = ?";
		return jdbcTemp.queryForObject(selectQuery, new RoleViewMapper(), new Object[] { id });
	}

	final class RoleViewMapper implements RowMapper<Role> {
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setId(rs.getLong("id"));
			role.setRole(rs.getString("role"));
			return role;
		}
	}

	public Role getRoleNameFromRoleId(long id) {
		String selectQuery = "select * FROM role where id = ?";
		return jdbcTemp.queryForObject(selectQuery, new RoleMapper(), new Object[] { id });
	}

	private SqlParameterSource getSqlParameterSource(String email) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		if (email != null) {
			parameterSource.addValue("email", email);
		}
		return parameterSource;
	}

	public Role getByRoleName(String roleName) {
		String selectQuery = "SELECT id, role FROM Role where role = ?";
		return jdbcTemp.queryForObject(selectQuery, new NoIdRoleMapper(), new Object[] { roleName });
	}

	private static final class NoIdRoleMapper implements RowMapper<Role> {
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setId(rs.getLong("id"));
			role.setRole(rs.getString("role"));
			return role;
		}
	}

	public Role updateARoleById(long id, String role) {
		Role updateRole = new Role();
		updateRole.setId(updateRole.getId());
		updateRole.setRole(role);
		String updateQuery = "update role set role=? where id=?";
		jdbcTemp.update(updateQuery, role, id);
		return updateRole;
	}

	public void deleteRoleById(long id) {
		String deleteQuery = "delete from role where id=?";
		jdbcTemp.update(deleteQuery, id);
	}

	public void deleteARole(long id, String role) {
		String deleteQuery = "delete from role where role=? and id<>1";
		jdbcTemp.update(deleteQuery, id);
		if (role != null && role.equals("ADMIN")) {
		}
	}

	public void deleteARoleRecord(Role role) {
		String deleteQuery = "delete from role where id = ?";
		jdbcTemp.update(deleteQuery, role);
	}

	public Role addARole(String role) {
		Role addRole = new Role();
		addRole.setRole(role);
		String insertQuery = "insert into role (role) values (?)";
		jdbcTemp.update(insertQuery, role);
		return addRole;
	}

}
