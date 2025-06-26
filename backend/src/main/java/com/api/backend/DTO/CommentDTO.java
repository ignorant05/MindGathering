package com.api.backend.DTO;

public class CommentDTO {

	private String content;

	public CommentDTO(String content) {
		this.content = content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

}
