package com.api.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.backend.entities.Comments;
import com.api.backend.entities.Users;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
	long countCommentsByAuthorId(long user_id);

	Users findAuthorByCommentId(long comment_id);

	List<Comments> findCommentsByAuthorId(long author_id);

	Comments findCommentByBlogId(long blog_id);
}
