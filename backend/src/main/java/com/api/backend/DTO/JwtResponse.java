package com.api.backend.DTO;

public class JwtResponse {

  private String access_token;
  private String refresh_token;
  private Long userId;
  private String username;
  private String email;

  public JwtResponse(String access_token, String refresh_token, Long userId, String username, String email) {
    this.access_token = access_token;
    this.refresh_token = refresh_token;
    this.userId = userId;
    this.username = username;
    this.email = email;
  }

  public void setAccessToken(String access_token) {
    this.access_token = access_token;
  }

  public void setRefreshToken(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAccessToken() {
    return this.access_token;
  }

  public String getRefreshToken() {
    return this.refresh_token;
  }

  public String getEmail() {
    return this.email;
  }

  public String getUsername() {
    return this.username;
  }

  public Long getUserId() {
    return this.userId;
  }
}
