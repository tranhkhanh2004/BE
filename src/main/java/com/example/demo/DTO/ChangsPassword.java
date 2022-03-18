package com.example.demo.DTO;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ChangsPassword {
	@Column(nullable=false)
	private String oldpassword;
	@Column(nullable=false)
	private String username;
	@Column(nullable=false)
	private String Password;
	@Column(nullable=false)
	private String rePassword;
	public String getOldpassword() {
		return oldpassword;
	}
	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getRePassword() {
		return rePassword;
	}
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}
	public String getusername() {
		return username;
	}
	public void setusername(String username) {
		this.username = username;
	}
	
	
	
}
