package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "administrator")
public class Administrator extends Account {	
//	public enum Status {
//		NEW,
//		RESET_PASSWORD,
//		ACTIVE,
//		INACTIVE
//	}
//	private String number;
//	private String password;
	private String name;
	private String title;
//	private Status status;
	
//	private String loginToken;
	
//	public String getNumber() {
//		return number;
//	}
//
//	public void setNumber(String number) {
//		this.number = number;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

//	public Status getStatus() {
//		return status;
//	}
//
//	public void setStatus(Status status) {
//		this.status = status;
//	}

//	public String getLoginToken() {
//		return loginToken;
//	}
//
//	public void setLoginToken(String loginToken) {
//		this.loginToken = loginToken;
//	}
}
