package com.api.backend.repositories;

import java.util.Optional;

import com.api.backend.entities.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByUsername(String username);

  Optional<Users> findById(Long uid);

}
