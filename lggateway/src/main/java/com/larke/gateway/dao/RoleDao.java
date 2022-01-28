package com.larke.gateway.dao;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;

import com.larke.gateway.model.Role;

@ComponentScan(basePackages = "com.larke.gateway")
public interface RoleDao {

	public void deleteRoleById(long id);

	public Role updateARoleById(long id, String role);

	public Role getByRoleName(String roleName);

	public Role getRoleNameFromRoleId(long id);

	public List<Role> findAllRoles();

	public void deleteARole(long id, String role);

	public Role getARecordByRoleId(long id);

	public void deleteARoleRecord(Role role);

	public Role AddARole(String role);

}
