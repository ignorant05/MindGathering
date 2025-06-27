package com.api.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Images {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "uid")
	private Users user;

	@Lob
	@Column(name = "image_data", columnDefinition = "BYTEA")
	private byte[] imageData;

	public Long getId() {
		return this.id;
	}

	public Users getUser() {
		return this.user;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public byte[] getData() {
		return this.imageData;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setData(byte[] data) {
		this.imageData = data;
	}
}
