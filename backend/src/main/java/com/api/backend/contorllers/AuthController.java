package com.api.backend.contorllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.DTO.EditUserDTO;
import com.api.backend.DTO.LoginDTO;
import com.api.backend.DTO.UserDTO;
import com.api.backend.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

  private UserService userServ;

  public AuthController(UserService userServ) {
    this.userServ = userServ;
  }

  @PostMapping(path = "/register")
  public ResponseEntity<?> resigter(@RequestBody UserDTO body) {
    try {
      return ResponseEntity
          .ok(userServ.registrationService(body));
    } catch (AuthenticationException authEx) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping(path = "/login")
  public ResponseEntity<?> login(@RequestBody LoginDTO request, HttpServletResponse response) {
    try {
      return ResponseEntity
          .ok(userServ.loginService(request, response));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("Invalid username/password : \n" + authEx);
    }
  }

  @GetMapping("/profile")
  public ResponseEntity<?> profile(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(userServ.profileService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("Invalid username/password : \n" + authEx);
    }
  }

  @GetMapping("/my/image")
  public ResponseEntity<?> getMyPic(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(userServ.getMyProfilePicture(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("Invalid username/password : \n" + authEx);
    }
  }

  @GetMapping("/users/{userId}/image")
  public ResponseEntity<?> getUserPic(@RequestHeader("Authorization") String authHeader,
      @PathVariable Long userId) {
    try {
      return ResponseEntity
          .ok(userServ.getUserProfilePicture(authHeader, userId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("Invalid username/password : \n" + authEx);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(userServ.logoutService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("Invalid username/password : \n" + authEx);
    }
  }

  @PostMapping("/delete")
  public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(userServ.deleteAccountService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("Invalid username/password : \n" + authEx);
    }
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateAccount(@RequestHeader("Authorization") String authHeader,
      @RequestBody EditUserDTO dto) {
    try {
      return ResponseEntity
          .ok(userServ.editUserProfileService(authHeader, dto));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("Invalid username/password : \n" + authEx);
    }
  }

  @GetMapping(path = "/refresh")
  public ResponseEntity<?> refresh(@CookieValue(name = "refresh_token", required = false) String refresh_token,
      HttpServletResponse response) {
    try {
      return ResponseEntity
          .ok(userServ.refreshTokenService(refresh_token, response));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("Invalid username/password.");
    }
  }

}
