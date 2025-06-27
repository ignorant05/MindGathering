package com.api.backend.DTO;

import org.springframework.web.multipart.MultipartFile;

public class EditUserDTO {
	private String username;
	private String email;
	private String oldPassword;
	private String newPassword;
	private MultipartFile profilePic;

	public EditUserDTO(
			String username,
			String email,
			String oldPassword,
			String newPassword,
			MultipartFile image) {
		this.username = username;
		this.email = email;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.profilePic = image;
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

	public MultipartFile getProfilePic() {
		return this.profilePic;
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

	public void setProfilePic(MultipartFile image) {
		this.profilePic = image;
	}
}
