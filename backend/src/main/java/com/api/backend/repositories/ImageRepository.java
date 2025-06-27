package com.api.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.entities.Images;
import com.api.backend.entities.Users;

public interface ImageRepository extends JpaRepository<Images, Long> {

	Images findByUser(Users user);
}
