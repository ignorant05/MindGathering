package com.api.backend.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "uid")
  public Long uid;

  @Column(name = "username")
  public String username;

  @Column(name = "email")
  public String email;

  @Column(name = "password")
  public String password;

  @Column(name = "profile_picture")
  public Images image;

  @ManyToMany(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
  @Column(name = "my_roles")
  public Roles myRoles;

  @Column(name = "refresh_token")
  public String token;

  @CreationTimestamp
  @Column(nullable = false)
  private Instant created_at;

  @UpdateTimestamp
  @Column(nullable = false)
  private Instant updated_at;

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setMyRoles(Roles myRoles) {
    this.myRoles = myRoles;
  }

  public void setRefreshToken(String refresh_token) {
    this.token = refresh_token;
  }

  public void setCreationDate(Instant created_at) {
    this.created_at = created_at;
  }

  public void setUpdatedDate(Instant updated_at) {
    this.updated_at = updated_at;
  }

  public Instant getCreationDate() {
    return this.created_at;
  }

  public Instant getUpdatedDate() {
    return this.updated_at;
  }

  public Long getUid() {
    return this.uid;
  }

  public String getUsername() {
    return this.username;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPassword() {
    return this.password;
  }

  public Roles getMyRoles() {
    return this.myRoles;
  }

  public String getRefreshToken() {
    return this.token;
  }
}
