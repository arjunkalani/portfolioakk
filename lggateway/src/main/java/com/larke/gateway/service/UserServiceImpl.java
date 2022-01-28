package com.larke.gateway.service;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.larke.gateway.dao.UserDao;
import com.larke.gateway.model.Role;
import com.larke.gateway.model.User;
import com.larke.gateway.repository.RoleRepository;
import com.larke.gateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service("userService")
@Slf4j
@ComponentScan(basePackages = "com.larke.gateway")
public class UserServiceImpl implements UserService {

	static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public List list() {
		return userDao.list();
	}

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void create(User user) {
		userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		user.setEnabled(true);
		user.setAccountlocked(false);
		user.setAccountexpired(false);
		user.setCredentialsexpired(false);
		String format = "yyyy-MM-dd HH:mm:ss";
		user.setCreatedtime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(format)));
		user.setUpdatedtime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(format)));
		Role roleUser = roleRepository.findByRole("User");
		user.addRole(roleUser);
		userRepository.save(user);
	}

	@Override
	public void update(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(user.getActive());
		user.setUpdatedtime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		userRepository.save(user);
	}

	public List<User> listAll() {
		return userRepository.findAll();

	}

	public List<User> listAllUsersDAO() {
		return userDao.listAllUsersDAO();
	}

	public User getSingleUserById(long id) {
		return userRepository.findById(id).get();
	}

	public List<User> listUsersWithRoles() {
		return userDao.listUsersWithRoles();
	}

	public void updateAUserById(User user) {
		userDao.updateAUserById(user);
	}

	public User getUserById(long id) {
		return userDao.getUserById(id);
	}

	public User findUserByEmail(String email) {
		return userDao.findUserByEmail(email);

	}

	public void deleteAUserRecordById(long id) {
		userDao.deleteAUserRecordById(id);
	}

	public void deleteById(long id) {
		userRepository.deleteById(id);
	}

}
