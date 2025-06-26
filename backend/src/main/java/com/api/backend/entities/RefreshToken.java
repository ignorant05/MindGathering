package com.api.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import jakarta.persistence.Column;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long tokenId;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "uid")
  private Users user;

  @Column(name = "token", nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

  @Column(nullable = false)
  private Instant issuedAt;

  public void setUser(Users user) {
    this.user = user;
  }

  public void setRefreshToken(String token) {
    this.token = token;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }

  public void setIssuedAt(Instant issuedAt) {
    this.issuedAt = issuedAt;
  }

  public long getTokenId() {
    return this.tokenId;
  }

  public String getRefreshToken() {
    return this.token;
  }

  public Users getUser() {
    return this.user;
  }

  public Instant getExpiryDate() {
    return this.expiryDate;
  }

  public Instant getIssuedAt() {
    return this.issuedAt;
  }

}
