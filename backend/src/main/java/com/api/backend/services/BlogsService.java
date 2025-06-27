package com.api.backend.services;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.api.backend.DTO.BlogDTO;
import com.api.backend.entities.Blogs;
import com.api.backend.entities.Users;
import com.api.backend.repositories.BlogsRepository;
import com.api.backend.repositories.UserRepository;
import com.api.backend.utils.JwtUtils;

@Service
public class BlogsService {
  private BlogsRepository blogsRepo;
  private JwtUtils jwtUtils;
  private UserRepository userRepo;

  public BlogsService(BlogsRepository blogsRepo, UserRepository userRepo, JwtUtils jwtUtils) {
    this.blogsRepo = blogsRepo;
    this.userRepo = userRepo;
    this.jwtUtils = jwtUtils;
  }

  public ResponseEntity<?> paginationService(String authHeader, Pageable pageable) {
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

      Page<Blogs> blogs = blogsRepo.findAll(pageable);

      return ResponseEntity.ok().body(blogs);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> newBlogService(String authHeader, BlogDTO dto) {
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

      var draft = new Blogs();

      draft.setTitle(dto.getTitle());
      draft.setContent(dto.getContent());
      draft.setAuthor(user);
      draft.setCreationDate(Instant.now());

      blogsRepo.save(draft);

      return ResponseEntity.ok("Created");

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> editBlogService(String authHeader, BlogDTO dto, Long blogId) {
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

      Blogs draft = blogsRepo.findBlogByBlogId(blogId);

      draft.setTitle(dto.getTitle());
      draft.setContent(dto.getContent());
      draft.setAuthor(user);
      draft.setUpdatedDate(Instant.now());

      blogsRepo.save(draft);

      return ResponseEntity.ok("Updated");

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> deleteBlogService(String authHeader, Long blogId) {
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

      blogsRepo.deleteById(blogId);

      return ResponseEntity.ok("deleted");

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> countAllBlogsService(String authHeader) {
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

      long number = blogsRepo.count();

      return ResponseEntity.ok().body(number);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> countMyBlogsService(String authHeader) {
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

      long number = blogsRepo.countBlogsByAuthorId(user.getUid());

      return ResponseEntity.ok().body(number);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> countUserBlogsService(String authHeader, Long author_id) {
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

      long number = blogsRepo.countBlogsByAuthorId(author_id);

      return ResponseEntity.ok().body(number);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> getAllBlogsService(String authHeader) {
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

      List<Blogs> blogs = blogsRepo.findAll();

      return ResponseEntity.ok().body(blogs);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> getMyBlogsService(String authHeader) {
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

      List<Blogs> blogs = blogsRepo.findBlogsByAuthorId(user.getUid());

      return ResponseEntity.ok().body(blogs);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> getUserBlogsService(String authHeader, Long author_id) {
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

      List<Blogs> blogs = blogsRepo.findBlogsByAuthorId(author_id);

      return ResponseEntity.ok().body(blogs);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> viewBlogService(String authHeader, Long blog_id) {
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

      Blogs blog = blogsRepo.findBlogByBlogId(blog_id);

      return ResponseEntity.ok().body(blog);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }
}
