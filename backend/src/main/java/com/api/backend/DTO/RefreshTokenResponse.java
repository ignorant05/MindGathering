package com.api.backend.DTO;

public class RefreshTokenResponse {
  private String access_token;
  private String refresh_token;

  public RefreshTokenResponse(String access_token, String refresh_token) {
    this.access_token = access_token;
    this.refresh_token = refresh_token;
  }

  public void setAccessToken(String access_token) {
    this.access_token = access_token;
  }

  public void setRefreshToken(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public String getAccessToken() {
    return this.access_token;
  }

  public String getRefreshToken() {
    return this.refresh_token;
  }
}
