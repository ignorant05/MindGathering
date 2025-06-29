
package com.api.backend.DTO;

public class ProfileResponse {

  private String username;
  private String email;

  public ProfileResponse(String username, String email) {
    this.username = username;
    this.email = email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return this.email;
  }

  public String getUsername() {
    return this.username;
  }
}
