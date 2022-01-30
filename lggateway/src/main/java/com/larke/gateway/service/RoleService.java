package com.larke.gateway.service;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import com.larke.gateway.model.Role;

@ComponentScan(basePackages = "com.larke.gateway")
public interface RoleService {

	public List<Role> listAll();

	void saveRole(Role role);

	public Role findById(long id);

	public Role getRoleNameFromRoleId(long id);

	public List<Role> findAllRoles();

	public Role updateARoleById(long id, String role);

	public void deleteRoleById(long id);

	public void deleteARole(long id, String role);

	public Role getByRoleName(String roleName);

	public Role getARecordByRoleId(long id);

	public void deleteRole(Role role);

	public Role addARole(String role);

	public Role findByRole(String role);

	public void deleteARoleRecord(Role role);

}
