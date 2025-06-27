package com.api.backend.services;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.api.backend.DTO.CommentDTO;
import com.api.backend.entities.Blogs;
import com.api.backend.entities.Comments;
import com.api.backend.entities.Users;
import com.api.backend.repositories.BlogsRepository;
import com.api.backend.repositories.CommentsRepository;
import com.api.backend.repositories.UserRepository;
import com.api.backend.utils.JwtUtils;

@Service
public class CommentsService {
  private CommentsRepository commentsRepo;
  private BlogsRepository blogRepo;
  private JwtUtils jwtUtils;
  private UserRepository userRepo;

  public CommentsService(CommentsRepository commentsRepo, BlogsRepository blogRepo, UserRepository userRepo,
      JwtUtils jwtUtils) {
    this.commentsRepo = commentsRepo;
    this.blogRepo = blogRepo;
    this.userRepo = userRepo;
    this.jwtUtils = jwtUtils;
  }

  public ResponseEntity<?> newCommentService(String authHeader, CommentDTO dto, Long blogId) {
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

      Blogs blog = blogRepo.findById(blogId)
          .orElseThrow(() -> new RuntimeException("Blog not found with id : " + blogId));

      if (blog == null) {
        return ResponseEntity.status(404).body("No blog found");
      }

      var draft = new Comments();

      draft.setContent(dto.getContent());
      draft.setAuthor(user);
      draft.setBlog(blog);
      draft.setCreationDate(Instant.now());

      commentsRepo.save(draft);

      return ResponseEntity.ok("Created");

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> editCommentService(String authHeader, CommentDTO dto, Long commentId, Long blogId) {
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

      Comments comment = commentsRepo.findById(commentId)
          .orElseThrow(() -> new RuntimeException("comment not found with id: " + commentId));

      if (comment == null) {
        return ResponseEntity.status(404).body("No comment found");
      }

      comment.setContent(dto.getContent());
      comment.setUpdatedDate(Instant.now());

      commentsRepo.save(comment);

      return ResponseEntity.ok("Created");

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> deleteCommentService(String authHeader, Long commentId, Long blogId) {
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

      Blogs blog = blogRepo.findById(blogId)
          .orElseThrow(() -> new RuntimeException("Blog not found with id : " + blogId));

      if (blog == null) {
        return ResponseEntity.status(404).body("No blog found");
      }

      commentsRepo.deleteById(commentId);

      return ResponseEntity.ok("deleted");

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> countAllCommentsService(String authHeader) {
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

      long number = commentsRepo.count();

      return ResponseEntity.ok().body(number);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> countMyCommentsService(String authHeader) {
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

      long number = commentsRepo.countCommentsByAuthorId(user.getUid());

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

      long number = commentsRepo.countCommentsByAuthorId(author_id);

      return ResponseEntity.ok().body(number);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> getAllCommentsService(String authHeader) {
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

      List<Comments> comments = commentsRepo.findAll();

      return ResponseEntity.ok().body(comments);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> getMyCommentssService(String authHeader) {
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

      List<Comments> comments = commentsRepo.findCommentsByAuthorId(user.getUid());

      return ResponseEntity.ok().body(comments);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }

  public ResponseEntity<?> getUserCommentsService(String authHeader, Long author_id) {
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

      List<Comments> comments = commentsRepo.findCommentsByAuthorId(author_id);

      return ResponseEntity.ok().body(comments);
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
    }
  }
}
