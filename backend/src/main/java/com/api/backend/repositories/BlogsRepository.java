package com.api.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.backend.entities.Blogs;
import com.api.backend.entities.Users;

@Repository
public interface BlogsRepository extends JpaRepository<Blogs, Long> {
	long countBlogsByAuthorId(long user_id);

	Users findAuthorByBlogId(long blog_id);

	List<Blogs> findBlogsByAuthorId(long author_id);

	Blogs findBlogByBlogId(long blog_id);
}
