package com.api.backend.DTO;

public class EditUserDTO {
	private String username;
	private String email;
	private String oldPassword;
	private String newPassword;

	public EditUserDTO(
			String username,
			String email,
			String oldPassword,
			String newPassword) {
		this.username = username;
		this.email = email;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public String getUsername() {
		return this.username;
	}

	public String getEmail() {
		return this.email;
	}

	public String getOldPassword() {
		return this.oldPassword;
	}

	public String getNewPassword() {
		return this.newPassword;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
