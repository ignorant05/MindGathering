package com.api.backend.contorllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.DTO.BlogDTO;
import com.api.backend.DTO.CommentDTO;
import com.api.backend.services.BlogsService;
import com.api.backend.services.CommentsService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
  private BlogsService blogsServ;
  private CommentsService commentsServ;

  public UserController(BlogsService blogsServ, CommentsService commentsServ) {
    this.blogsServ = blogsServ;
    this.commentsServ = commentsServ;
  }

  @GetMapping("/pages")
  public ResponseEntity<?> pagination(@RequestHeader("Authorization") String authHeader,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "0") int page) {
    try {
      Pageable pageable = PageRequest
          .of(page, size, Sort.by("updated_at").descending());
      return ResponseEntity
          .ok(blogsServ.paginationService(authHeader, pageable));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/get/blogs")
  public ResponseEntity<?> getAllBlogs(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(blogsServ.getAllBlogsService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/get/my/blogs")
  public ResponseEntity<?> getMyBlogs(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(blogsServ.getMyBlogsService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/count/blogs")
  public ResponseEntity<?> countAllBlogs(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(blogsServ.countAllBlogsService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/count/my/blogs")
  public ResponseEntity<?> countMyBlogs(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(blogsServ.countMyBlogsService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/count/my/blogs")
  public ResponseEntity<?> countUserBlogs(@RequestHeader("Authorization") String authHeader,
      @RequestParam long userId) {
    try {
      return ResponseEntity
          .ok(blogsServ.countUserBlogsService(authHeader, userId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to get blogs : \n" + authEx);
    }
  }

  @GetMapping("/get/users/blogs")
  public ResponseEntity<?> getUserBlogs(@RequestHeader("Authorization") String authHeader,
      @RequestParam long userId) {
    try {
      return ResponseEntity
          .ok(blogsServ.getUserBlogsService(authHeader, userId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to get blogs : \n" + authEx);
    }
  }

  @GetMapping("/get/blogs")
  public ResponseEntity<?> getBlog(@RequestHeader("Authorization") String authHeader, @RequestParam long blogId) {
    try {
      return ResponseEntity
          .ok(blogsServ.viewBlogService(authHeader, blogId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to get blog : \n" + authEx);
    }
  }

  @PostMapping("/create/blog")
  public ResponseEntity<?> newBlog(@RequestHeader("Authorization") String authHeader, @RequestBody BlogDTO dto) {
    try {
      return ResponseEntity
          .ok(blogsServ.newBlogService(authHeader, dto));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to create new blog: \n" + authEx);
    }
  }

  @PutMapping("/update/blogs")
  public ResponseEntity<?> editBlog(@RequestHeader("Authorization") String authHeader, @RequestBody BlogDTO dto,
      @RequestParam long blogId) {
    try {
      return ResponseEntity
          .ok(blogsServ.editBlogService(authHeader, dto, blogId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to update blog : \n" + authEx);
    }
  }

  @DeleteMapping("/delete/blogs")
  public ResponseEntity<?> deleteBlog(@RequestHeader("Authorization") String authHeader, @RequestParam long blogId) {
    try {
      return ResponseEntity
          .ok(blogsServ.deleteBlogService(authHeader, blogId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to delete blog : \n" + authEx);
    }
  }

  @GetMapping("/get/all/comments")
  public ResponseEntity<?> getAllComments(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(commentsServ.getAllCommentsService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/get/my/comments")
  public ResponseEntity<?> getMyComments(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(commentsServ.getMyCommentssService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/count/all/comments")
  public ResponseEntity<?> countAllComments(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(commentsServ.countAllCommentsService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/count/my/blogs")
  public ResponseEntity<?> countMyComments(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(commentsServ.countMyCommentsService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to count blogs : \n" + authEx);
    }
  }

  @GetMapping("/count/my/comments")
  public ResponseEntity<?> countUserBlogs(@RequestHeader("Authorization") String authHeader) {
    try {
      return ResponseEntity
          .ok(commentsServ.getMyCommentssService(authHeader));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to get blogs : \n" + authEx);
    }
  }

  @GetMapping("/get/users/{userId}/blogs")
  public ResponseEntity<?> getUserComments(@RequestHeader("Authorization") String authHeader,
      @PathVariable long userId) {
    try {
      return ResponseEntity
          .ok(commentsServ.getUserCommentsService(authHeader, userId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to get blogs : \n" + authEx);
    }
  }

  @PostMapping("/create/blogs/{blogId}/comment")
  public ResponseEntity<?> newComment(@RequestHeader("Authorization") String authHeader, @RequestBody CommentDTO dto,
      @PathVariable long blogId) {
    try {
      return ResponseEntity
          .ok(commentsServ.newCommentService(authHeader, dto, blogId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to create new blog: \n" + authEx);
    }
  }

  @PutMapping("/update/blogs/{blogId}/comments")
  public ResponseEntity<?> editComment(@RequestHeader("Authorization") String authHeader, @RequestBody CommentDTO dto,
      @PathVariable long blogId, @RequestParam long commentId) {
    try {
      return ResponseEntity
          .ok(commentsServ.editCommentService(authHeader, dto, blogId, commentId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to update blog : \n" + authEx);
    }
  }

  @DeleteMapping("/delete/blogs/{blogId}/comments")
  public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String authHeader, @PathVariable long blogId,
      @RequestParam long commentId) {
    try {
      return ResponseEntity
          .ok(commentsServ.deleteCommentService(authHeader, blogId, commentId));
    } catch (AuthenticationException authEx) {
      throw new RuntimeException("unable to delete blog : \n" + authEx);
    }
  }
}
