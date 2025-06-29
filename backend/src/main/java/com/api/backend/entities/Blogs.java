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
@Table(name = "blogs")
public class Blogs {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bid;

	@Column(nullable = false, unique = true)
	private String title;

	@ManyToOne
	@JoinColumn(name = "author_id", referencedColumnName = "uid")
	private Users author;

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

	public void setTitle(String title) {
		this.title = title;
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

	public Long getBid() {
		return this.bid;
	}

	public Users getAuthor() {
		return this.author;
	}

	public String getTitle() {
		return this.title;
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
