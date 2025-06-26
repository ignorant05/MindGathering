package com.api.backend.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

import com.api.backend.utils.RolesEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Roles {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "user_role", nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private RolesEnum userRole;

  @Column(name = "role_description")
  private String roleDescription;

  @CreationTimestamp
  @Column(name = "created_at")
  private Date createdAt;

  @UpdateTimestamp
  @Column(name = "last_updated_date")
  private Date updatedAt;

  public void setUserRole(RolesEnum userRole) {
    this.userRole = userRole;
  }

  public void setUserRoleDescription(String description) {
    this.roleDescription = description;
  }

  public void setCreationDate(Date createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getRoleId() {
    return this.id;
  }

  public String getRoleDescription() {
    return this.roleDescription;
  }

  public RolesEnum getRole() {
    return this.userRole;
  }

  public Date getCreationDate() {
    return this.createdAt;
  }

  public Date getUpdatedAt() {
    return this.updatedAt;
  }
}
