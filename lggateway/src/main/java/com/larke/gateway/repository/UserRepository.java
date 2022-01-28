package com.larke.gateway.repository;

import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.tokens.Token.ID;

import com.larke.gateway.model.User;

@Repository
@Transactional
@ComponentScan(basePackages = "com.larke.gateway")
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findById(ID id);

	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
	public User findByVerificationCode(String code);

	@Query("SELECT u FROM User u WHERE u.email = ?1")
	User findByEmail(String email);

}