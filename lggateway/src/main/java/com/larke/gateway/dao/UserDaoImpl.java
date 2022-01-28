package com.larke.gateway.dao;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import com.larke.gateway.mapper.EmailMapper;
import com.larke.gateway.mapper.RegisterUserMapper;
import com.larke.gateway.mapper.UserMapper;
import com.larke.gateway.model.Role;
import com.larke.gateway.model.User;
import com.larke.gateway.repository.RoleRepository;
import com.larke.gateway.repository.UserRepository;

@ComponentScan(basePackages = "com.larke.gateway")
@Repository
public class UserDaoImpl implements UserDao {

	static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
	static final String LISTALLBYEMAILQUERY = "SELECT * from User WHERE email=?";
	static final String LISTONEROLEQUERY = "select * from User where User = :user, id = ?";

	@Autowired
	private JdbcTemplate jdbcTemp;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	public UserDaoImpl(DataSource dataSource) {
		jdbcTemp = new JdbcTemplate(dataSource);
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemp) {
		this.jdbcTemp = jdbcTemp;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public void add(String email) {
		String sql = "insert into user(email) values(:email)";
		jdbcTemp.update(sql, getSqlParameterSource(email, " "));
		sql = "insert into user_roles(email, role) values(:email, 'USER')";
		jdbcTemp.update(sql, getSqlParameterSource(email, " "));
	}

	public User getUserById(long id) {
		String selectQuery = "select id, firstname, lastname, email, password, updatedtime from user where id=?";
		LOGGER.info("Getting Record with ID = ", id);
		return jdbcTemp.queryForObject(selectQuery, new UserViewMapper(), new Object[] { id });
	}

	final class UserViewMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getLong("id"));
			user.setFirstname(rs.getString("firstname"));
			user.setLastname(rs.getString("lastname"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setUpdatedtime(rs.getString("updatedtime"));
			return user;
		}
	}

	public void deleteAUserRecordById(long id) {
		String deleteQuery = "delete from user where id=?";
		Object[] params = { id };
		int rows = jdbcTemp.update(deleteQuery, params);
		LOGGER.info(rows + " row(s) deleted.");
	}

	public void updateAUserById(User user) {
		user.setId(user.getId());
		user.setFirstname(user.getFirstname());
		user.setLastname(user.getLastname());
		user.setEmail(user.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role roleUser = roleRepository.findByRole("Roles");
		user.addRole(roleUser);
		user.setEnabled(false);
		user.setAccountlocked(false);
		user.setAccountexpired(false);
		user.setCredentialsexpired(false);
		user.setUpdatedtime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		userRepository.save(user);
	}

	public List<User> listAllUsersDAO() {
		String selectQuery = "SELECT * from User";
		return jdbcTemp.query(selectQuery, new UserMapper());
	}

	private SqlParameterSource getSqlParameterSource(String email, String password) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		if (email != null) {
			parameterSource.addValue("email", email);
		}
		if (password != null) {
			parameterSource.addValue("password", password);
		}
		return parameterSource;
	}

	public boolean userExists(String email) {
		String sql = "select * from user where email = ?";
		List list = jdbcTemp.queryForList(sql, getSqlParameterSource(email, " "), new EmailMapper());
		if (!list.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public User getSingleUserById(Long id) {
		String selectQuery = "select * FROM user WHERE id = ?";
		return jdbcTemp.queryForObject(selectQuery, new UserMapper(), new Object[] { id });
	}

	public void registerAUserByEmail(String email) {
		String insertQuery = "insert into user (email, password, firstname, lastname) values (?, ?, ? ?)";
		jdbcTemp.update(insertQuery, new RegisterUserMapper());

	}

	public List<User> listUsersWithRoles() {
		String selectQuery = "select * from user";
		List listUsers = jdbcTemp.queryForList(selectQuery, new UserRolesMapper());
		return listUsers;
	}

	private static final class UserRolesMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setRoles((Set<Role>) rs.getArray("roles"));
			LOGGER.info("REPORTING FROM UserRoleMapper: " + user.getRoles());
			return user;
		}
	}

	private static final class UserRoleMapper implements RowMapper<User> {
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
			user.setRoles((Set<Role>) rs.getArray("roles"));
			LOGGER.info("REPORTING FROM UserRoleMapper: " + user.getRoles());
			return user;
		}
	}

	public List<Role> findAllRoles() {
		String selectQuery = "select id, user_id, role_id from user_role";
		return this.jdbcTemp.query(selectQuery, new RoleMapper());
	}

	private static final class RoleMapper implements RowMapper<Role> {
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setId(rs.getLong("id"));
			role.setRole(rs.getString("role"));
			return role;
		}
	}

	public User findUserByEmail(String email) {
		String selectQuery = "select email, password, roles from user where email = ?";
		User user = (User) jdbcTemp.queryForObject(selectQuery, new Object[] { email, null, null },
				new UserEmailMapper());
		return user;
	}

	private static final class UserEmailMapper implements RowMapper {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	}

	public List list() {
		String selectQuery = "select email from user";
		List list = jdbcTemp.queryForList(selectQuery, getSqlParameterSource(null, null), new UserEmailMapper());
		return list;
	}

}
