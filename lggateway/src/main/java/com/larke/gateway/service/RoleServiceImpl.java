package com.larke.gateway.service;

import java.util.List;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import com.larke.gateway.dao.RoleDao;
import com.larke.gateway.model.Role;
import com.larke.gateway.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;

@Service("roleService")
@Slf4j
@ComponentScan(basePackages = "com.larke.gateway")
public class RoleServiceImpl implements RoleService {

	static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleDao roleDao;

	@Override
	public List<Role> listAll() {
		return roleDao.findAllRoles();
	}

	public List<Role> findAllRoles(Set<Role> set) {
		return roleDao.findAllRoles();

	}

	public Role findByRole(String role) {
		return roleRepository.findByRole(role);
	}

	public Role getRoleNameFromRoleId(long id) {
		return roleDao.getRoleNameFromRoleId(id);

	}

	public void deleteRole(Role role) {
		roleRepository.delete(role);
	}

	public Role getByRoleName(String roleName) {
		return roleDao.getByRoleName(roleName);
	}

	public Role getARecordByRoleId(long id) {
		return roleDao.getARecordByRoleId(id);

	}

	public Role findById(long id) {
		return roleRepository.findById(id).get();
	}

	public void deleteARole(long id, String role) {
		roleDao.deleteARole(id, role);
	}

	public void deleteRoleById(long id) {
		roleRepository.deleteById(id);
	}

	@Override
	public void saveRole(Role role) {
		roleRepository.save(role);
	}

	public List<Role> findAllRoles() {
		return roleDao.findAllRoles();
	}

	public Role updateARoleById(long id, String role) {
		return roleDao.updateARoleById(id, role);
	}

	public Role AddARole(String role) {
		return roleDao.AddARole(role);
	}

	public void deleteARoleRecord(Role role) {
		roleDao.deleteARoleRecord(role);
	}

}
