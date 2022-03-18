package com.example.demo.DTO;

import java.util.Date;

import javax.persistence.Column;

import lombok.Data;

@Data
public class ChangProfile {
	@Column(nullable=false)
	private String Fullname;
	@Column(nullable=false)
	private Date Birth;
	@Column(nullable=false)
	private String Address;
	@Column(nullable=false)
	private String email;
	@Column(nullable=false)
	private String Phone;
	@Column(nullable=false)
	private int Sex;
	@Column(nullable=false)
	private String username;
	public String getFullname() {
		return Fullname;
	}
	public void setFullname(String fullname) {
		Fullname = fullname;
	}
	public Date getBirth() {
		return Birth;
	}
	public void setBirth(Date birth) {
		Birth = birth;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public int getSex() {
		return Sex;
	}
	public void setSex(int sex) {
		Sex = sex;
	}
	public String getusername() {
		return username;
	}
	public void setusername(String username) {
		this.username = username;
	}
	
	
	
}
