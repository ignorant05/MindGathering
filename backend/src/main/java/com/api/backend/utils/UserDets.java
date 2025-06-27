package com.api.backend.utils;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.api.backend.entities.Users;

import org.springframework.security.core.GrantedAuthority;

public class UserDets implements UserDetails {
  private String username;
  private String password;
  private String email;
  private String refresh_token;
  private Long uid;
  private List<GrantedAuthority> authorities;

  public UserDets(Users user) {
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.uid = user.getUid();
    this.password = user.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  public Long getUid() {
    return this.uid;
  }

  public String getEmail() {
    return this.email;
  }

  public String getRefreshToken() {
    return this.refresh_token;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
