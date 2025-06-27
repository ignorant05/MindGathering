package com.api.backend.services;

import org.springframework.stereotype.Service;

import com.api.backend.DTO.EditUserDTO;
import com.api.backend.DTO.JwtResponse;
import com.api.backend.DTO.LoginDTO;
import com.api.backend.DTO.RefreshTokenResponse;
import com.api.backend.DTO.UserDTO;
import com.api.backend.DTO.ProfileResponse;
import com.api.backend.entities.Images;
import com.api.backend.entities.RefreshToken;
import com.api.backend.entities.Users;
import com.api.backend.errors.RefreshTokenException;
import com.api.backend.repositories.ImageRepository;
import com.api.backend.repositories.UserRepository;
import com.api.backend.utils.JwtUtils;
import com.api.backend.utils.UserDets;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
  private UserRepository userRepo;
  private ImageRepository imageRepo;
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private PasswordEncoder encoder;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private RefreshTokenService refreshTokenService;

  public UserService(UserRepository userRepo, ImageRepository imageRepo) {
    this.imageRepo = imageRepo;
    this.userRepo = userRepo;
  }

  public String registrationService(UserDTO dto) {
    String encodedPass = encoder.encode(dto.getPassword());
    System.out.println(encodedPass);

    var user = new Users();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    user.setPassword(encodedPass);
    user.setCreationDate(Instant.now());

    userRepo.save(user);

    return "Registered";
  }

  public ResponseEntity<?> loginService(LoginDTO dto, HttpServletResponse response) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      Object principal = authentication.getPrincipal();
      if (!(principal instanceof UserDets) || principal == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user details");
      }

      UserDets userDets = (UserDets) principal;

      System.out.println("the current dets: " + userDets);

      String jwt = jwtUtils.generateToken(userDets.getUid());
      RefreshToken refreshToken = refreshTokenService
          .generateRefreshToken(userDets.getUid());

      Cookie cookie = new Cookie("refresh_token", refreshToken.getRefreshToken());
      cookie.setHttpOnly(true);
      cookie.setSecure(false);
      cookie.setPath("/");
      cookie.setMaxAge(3600);

      response.addCookie(cookie);
      response.setHeader("Authorization", "Bearer " + jwt);

      return ResponseEntity.ok(new JwtResponse(
          jwt,
          refreshToken.getRefreshToken(),
          userDets.getUid(),
          userDets.getUsername(),
          userDets.getEmail()));
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

  public ResponseEntity<?> refreshTokenService(String refresh_token, HttpServletResponse response) {
    RefreshToken token = refreshTokenService.findByToken(refresh_token)
        .map(refreshTokenService::expired)
        .orElseThrow(() -> new RefreshTokenException(refresh_token,
            "Refresh token is not in database!"));

    Users user = token.getUser();
    String accessToken = jwtUtils.generateToken(user.getUid());

    response.setHeader("Authorization", "Bearer " + accessToken);

    return ResponseEntity.ok(new RefreshTokenResponse(accessToken, refresh_token));
  }

  public ResponseEntity<?> profileService(String authHeader) {
    try {
      String accessToken = authHeader.substring(7).trim();
      boolean valid = accessToken != null && jwtUtils.validateJwtToken(accessToken)
          && !jwtUtils.isTokenExpired(accessToken);

      if (!valid) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid access token");
      }

      String username = jwtUtils.extractUsername(accessToken);

      Users user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

      if (user == null) {
        return ResponseEntity.status(404).body("No such user");
      }

      return ResponseEntity.ok(new ProfileResponse(
          user.getUsername(),
          user.getEmail()));
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> getUserProfilePicture(String authHeader, Long userId) {
    try {
      String accessToken = authHeader.substring(7).trim();
      boolean valid = accessToken != null && jwtUtils.validateJwtToken(accessToken)
          && !jwtUtils.isTokenExpired(accessToken);

      if (!valid) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid access token");
      }

      String username = jwtUtils.extractUsername(accessToken);

      Users user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

      if (user == null) {
        return ResponseEntity.status(404).body("No such user");
      }

      Users target = userRepo.findById(userId)
          .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

      if (target == null) {
        return ResponseEntity.status(404).body("No such user");
      }

      Images image = imageRepo.findByUser(target);

      if (image == null || image.getData() == null) {
        return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok()
          .contentType(MediaType.IMAGE_JPEG)
          .body(image.getData());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> getMyProfilePicture(String authHeader) {
    try {
      String accessToken = authHeader.substring(7).trim();
      boolean valid = accessToken != null && jwtUtils.validateJwtToken(accessToken)
          && !jwtUtils.isTokenExpired(accessToken);

      if (!valid) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid access token");
      }

      String username = jwtUtils.extractUsername(accessToken);

      Users user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

      if (user == null) {
        return ResponseEntity.status(404).body("No such user");
      }

      Images image = imageRepo.findByUser(user);

      if (image == null || image.getData() == null) {
        return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok()
          .contentType(MediaType.IMAGE_JPEG)
          .body(image.getData());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> editUserProfileImageService(String authHeader, EditUserDTO dto) {
    try {
      String accessToken = authHeader.substring(7).trim();
      boolean valid = accessToken != null && jwtUtils.validateJwtToken(accessToken)
          && !jwtUtils.isTokenExpired(accessToken);

      if (!valid) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid access token");
      }

      String username = jwtUtils.extractUsername(accessToken);

      Users user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

      if (user == null) {
        return ResponseEntity.status(404).body("No such user");
      }

      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getOldPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      Object principal = authentication.getPrincipal();
      if (!(principal instanceof UserDets) || principal == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user details");
      }

      if (dto.getNewPassword() != "") {
        user.setPassword(dto.getNewPassword());
      }

      user.setUsername(dto.getUsername());
      user.setEmail(dto.getEmail());
      user.setUpdatedDate(Instant.now());

      userRepo.save(user);

      return ResponseEntity.ok(new ProfileResponse(
          user.getUsername(),
          user.getEmail()));
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> editUserProfileService(String authHeader, EditUserDTO dto) {
    try {
      String accessToken = authHeader.substring(7).trim();
      boolean valid = accessToken != null && jwtUtils.validateJwtToken(accessToken)
          && !jwtUtils.isTokenExpired(accessToken);

      if (!valid) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid access token");
      }

      String username = jwtUtils.extractUsername(accessToken);

      Users user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

      if (user == null) {
        return ResponseEntity.status(404).body("No such user");
      }

      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getOldPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      Object principal = authentication.getPrincipal();
      if (!(principal instanceof UserDets) || principal == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user details");
      }

      if (dto.getNewPassword() != "") {
        user.setPassword(dto.getNewPassword());
      }

      if (!dto.getProfilePic().isEmpty()) {
        Images image = imageRepo.findByUser(user);
        if (image == null) {
          image = new Images();
          image.setUser(user);
        }
        image.setName(dto.getProfilePic().getOriginalFilename());
        image.setType(dto.getProfilePic().getContentType());

        byte[] data;
        try {
          data = dto.getProfilePic().getBytes();
        } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("corrupted image");
        }
        image.setData(data);
        imageRepo.save(image);
      }

      user.setUsername(dto.getUsername());
      user.setEmail(dto.getEmail());
      user.setUpdatedDate(Instant.now());

      userRepo.save(user);

      return ResponseEntity.ok(new ProfileResponse(
          user.getUsername(),
          user.getEmail()));
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> logoutService(String authHeader) {
    try {
      String accessToken = authHeader.substring(7);
      boolean valid = accessToken != null && jwtUtils.validateJwtToken(accessToken)
          && !jwtUtils.isTokenExpired(accessToken);

      if (!valid) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid access token");
      }

      String username = jwtUtils.extractUsername(accessToken);

      Users user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

      if (user == null) {
        return ResponseEntity.status(404).body("No such user");
      }

      refreshTokenService.deleteByUserId(user.getUid());

      return ResponseEntity.ok().body("Logged out");
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> deleteAccountService(String authHeader) {
    try {
      String accessToken = authHeader.substring(7);
      boolean valid = accessToken != null && jwtUtils.validateJwtToken(accessToken)
          && !jwtUtils.isTokenExpired(accessToken);

      if (!valid) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid access token");
      }

      String username = jwtUtils.extractUsername(accessToken);

      Users user = userRepo.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

      if (user == null) {
        return ResponseEntity.status(404).body("No such user");
      }

      refreshTokenService.deleteByUserId(user.getUid());
      userRepo.deleteById(user.getUid());

      return ResponseEntity.ok().body("User Deleted Successfully");
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

}
