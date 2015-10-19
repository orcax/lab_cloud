package com.prj.util;

public class PasswordReset {
	private Integer id;
	private AccountCharacter accountCharacter;
	private String oldPassword;
	private String newPassword;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public AccountCharacter getAccountCharacter() {
		return accountCharacter;
	}
	public void setAccountCharacter(AccountCharacter accountCharacter) {
		this.accountCharacter = accountCharacter;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
