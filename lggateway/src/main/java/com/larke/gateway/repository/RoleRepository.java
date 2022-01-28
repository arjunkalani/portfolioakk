package com.larke.gateway.repository;

import org.springframework.context.annotation.ComponentScan;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.larke.gateway.model.Role;

@ComponentScan(basePackages = "com.larke.gateway")
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query("SELECT r FROM Role r WHERE r.role = ?1")
	public Role findByRole(String role);

}