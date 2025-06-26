package com.api.backend.services;

import com.api.backend.entities.RefreshToken;
import com.api.backend.entities.Users;
import com.api.backend.errors.RefreshTokenException;
import com.api.backend.repositories.RefreshTokenRepository;
import com.api.backend.repositories.UserRepository;
import com.api.backend.utils.RefreshTokenUtils;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
  @Value("${refresh.token.expiration}")
  private Long REFRESH_TOKEN_EXP;

  private UserRepository userRepo;
  private RefreshTokenRepository refreshTokenRepo;
  @Autowired
  private RefreshTokenUtils refreshTokenUtils;

  public RefreshTokenService(RefreshTokenRepository refreshTokenRepo,
      UserRepository userRepo) {
    this.refreshTokenRepo = refreshTokenRepo;
    this.userRepo = userRepo;
  }

  public RefreshToken generateRefreshToken(String username) {
    Users user = userRepo.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

    RefreshToken existingToken = refreshTokenRepo.findByUser(user).orElse(null);

    if (existingToken != null) {
      existingToken.setRefreshToken(refreshTokenUtils.generateToken(username));
      existingToken.setIssuedAt(Instant.now());
      existingToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXP));
      return refreshTokenRepo.save(existingToken);
    }

    RefreshToken refresh_token = new RefreshToken();
    String generatedToken = refreshTokenUtils.generateToken(username);
    refresh_token.setUser(user);
    refresh_token.setIssuedAt(Instant.now());
    refresh_token.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXP));
    refresh_token.setRefreshToken(generatedToken);

    refresh_token = refreshTokenRepo.save(refresh_token);
    return refresh_token;
  }

  public RefreshToken expired(RefreshToken token) {
    String refresh_token = token.getRefreshToken();

    if (!refreshTokenUtils.validateJwtToken(refresh_token) || refreshTokenUtils.isTokenExpired(refresh_token)) {
      refreshTokenRepo.delete(token);
      throw new RefreshTokenException(token.getRefreshToken(),
          "Refresh token was expired. Please make a new signin request");
    }
    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    Users user = userRepo.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    return refreshTokenRepo.deleteByUser(user);
  }

  public Optional<RefreshToken> findByToken(String refresh_token) {
    return refreshTokenRepo.findByToken(refresh_token);
  }

}
