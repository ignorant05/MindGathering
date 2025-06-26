package com.api.backend.services;

//
// import java.util.Optional;
// import org.springframework.security.core.userdetails.User;
// import java.util.Collections;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.api.backend.repositories.UserRepository;
import com.api.backend.entities.Users;
import com.api.backend.utils.UserDets;

@Service
public class UserDetService implements UserDetailsService {

  public UserRepository userRepo;

  public UserDetService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users userRes = userRepo.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

    return new UserDets(userRes);
  }
}
