package com.api.backend.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comments {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long comment_id;

	@ManyToOne
	@JoinColumn(name = "author_id", referencedColumnName = "uid")
	private Users author;

	@ManyToOne
	@JoinColumn(name = "blog_id", referencedColumnName = "bid")
	private Blogs blog;

	@Column(nullable = false)
	private String content;

	@CreationTimestamp
	@Column(nullable = false)
	private Instant created_at;

	@UpdateTimestamp
	@Column(nullable = false)
	private Instant updated_at;

	public void setAuthor(Users author) {
		this.author = author;
	}

	public void setBlog(Blogs blog) {
		this.blog = blog;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreationDate(Instant created_at) {
		this.created_at = created_at;
	}

	public void setUpdatedDate(Instant updated_at) {
		this.updated_at = updated_at;
	}

	public Users getAuthor() {
		return this.author;
	}

	public Blogs getBlog() {
		return this.blog;
	}

	public String getContent() {
		return this.content;
	}

	public Instant getCreationDate() {
		return this.created_at;
	}

	public Instant getUpdatedDate() {
		return this.updated_at;
	}
}
