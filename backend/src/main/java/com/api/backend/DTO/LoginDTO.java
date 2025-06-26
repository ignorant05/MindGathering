package com.api.backend.DTO;

public class LoginDTO {
  private String email;
  private String username;
  private String password;

  public LoginDTO(
      String username,
      String password,
      String email) {
    this.email = email;
    this.username = username;
    this.password = password;
  }

  public String getEmail() {
    return this.email;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
