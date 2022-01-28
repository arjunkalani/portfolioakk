package com.larke.gateway.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.context.annotation.ComponentScan;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode
@Table(name = "user")
@ComponentScan(basePackages = "com.larke.gateway")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "verificationcode")
	private String verificationCode;

	@Column(name = "password")
	private String password;

	@Column(name = "active")
	private int active;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "accountlocked")
	private boolean accountlocked;

	@Column(name = "accountexpired")
	private boolean accountexpired;

	@Column(name = "credentialsexpired")
	private boolean credentialsexpired;

	@Column(name = "createdtime", updatable = false)
	private String createdtime;

	@Column(name = "updatedtime")
	private String updatedtime;

	@Column(name = "confirmationtoken")
	private String confirmationtoken;

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public void addRole(Role role) {
		this.roles.add(role);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountlocked() {
		return accountlocked;
	}

	public void setAccountlocked(boolean accountlocked) {
		this.accountlocked = accountlocked;
	}

	public boolean isAccountexpired() {
		return accountexpired;
	}

	public void setAccountexpired(boolean accountexpired) {
		this.accountexpired = accountexpired;
	}

	public boolean isCredentialsexpired() {
		return credentialsexpired;
	}

	public void setCredentialsexpired(boolean credentialsexpired) {
		this.credentialsexpired = credentialsexpired;
	}

	public String getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(String createdtime) {
		this.createdtime = createdtime;
	}

	public String getUpdatedtime() {
		return updatedtime;
	}

	public void setUpdatedtime(String updatedtime) {
		this.updatedtime = updatedtime;
	}

	public String getConfirmationtoken() {
		return confirmationtoken;
	}

	public void setConfirmationtoken(String confirmationtoken) {
		this.confirmationtoken = confirmationtoken;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public User() {
		super();
	}

	public User(String email) {
		super();
		this.email = email;
	}

	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public User(String email, String password, Set<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public User(long id, String email, String firstname, String lastname, String verificationCode, String password,
			int active, boolean enabled, boolean accountlocked, boolean accountexpired, boolean credentialsexpired,
			String createdtime, String updatedtime, String confirmationtoken, Set<Role> roles) {
		super();
		this.id = id;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.verificationCode = verificationCode;
		this.password = password;
		this.active = active;
		this.enabled = enabled;
		this.accountlocked = accountlocked;
		this.accountexpired = accountexpired;
		this.credentialsexpired = credentialsexpired;
		this.createdtime = createdtime;
		this.updatedtime = updatedtime;
		this.confirmationtoken = confirmationtoken;
		this.roles = roles;
	}

	public String getFullName() {
		return this.firstname + " " + this.lastname;
	}

	@Override
	public String toString() {
		return "User [userId=" + id + "]";
	}

}