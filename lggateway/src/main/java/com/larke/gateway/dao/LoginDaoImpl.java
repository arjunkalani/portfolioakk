package com.larke.gateway.dao;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
@Repository
public class LoginDaoImpl implements LoginDao {

	static final Logger LOGGER = LoggerFactory.getLogger(LoginDaoImpl.class);

	@Autowired
	private static JdbcTemplate jdbcTemp;

	public LoginDaoImpl(DataSource dataSource) {
		jdbcTemp = new JdbcTemplate(dataSource);
	}

	public User findUserInfo(String email) {
		String selectQuery = "select u.email email, u.password password, u.roles roles from user u, where u.email = ?";
		return (User) jdbcTemp.queryForObject(selectQuery, new Object[] { email, null, null }, new UserInfoMapper());
	}

	private static final class UserInfoMapper implements RowMapper {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			String email = rs.getString("email");
			String password = rs.getString("password");
			Array role = rs.getArray("roles");
			String[] roles = (String[]) role.getArray();
			return user;
		}
	}

	private SqlParameterSource getSqlParameterSource(long user_id, long role_id) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("user_id", user_id);
		parameterSource.addValue("role_id", role_id);
		return parameterSource;
	}

	@Override
	public List getUserRoles(long userid, long roleid) {
		String selectQuery = "select user_id, role_id from user_role where user_id = ?";
		return jdbcTemp.queryForList(selectQuery, getSqlParameterSource(userid, roleid), Long.class);
	}
}
