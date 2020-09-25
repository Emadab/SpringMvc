package com.example.demo.Model.SecurityModels;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "user_name")
	private String userName;
	@Column(name = "password")
	private String password;
	@Column(name = "role")
	private String role;
	@Column(name = "enabled")
	private boolean enabled;

	public User(String userName, String password, String role, boolean enabled){
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public User setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
}
