package com.api.backend.DTO;

public class BlogDTO {
	private String title;
	private String content;

	public BlogDTO(
			String title,
			String content) {
		this.title = content;
		this.content = content;
	}

	public String getTitle() {
		return this.title;
	}

	public String getContent() {
		return this.content;
	}

	public void setUsername(String title) {
		this.title = title;
	}

	public void setEmail(String content) {
		this.content = content;
	}
}
