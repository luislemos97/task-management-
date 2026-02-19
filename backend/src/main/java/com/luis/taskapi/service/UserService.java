package com.luis.taskapi.service;

import com.luis.taskapi.dto.RegisterRequest;
import com.luis.taskapi.entity.User;
import com.luis.taskapi.error.BadRequestException;
import com.luis.taskapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User register(RegisterRequest req) {
    if (userRepository.existsByUsername(req.username())) throw new BadRequestException("username already exists");
    if (userRepository.existsByEmail(req.email())) throw new BadRequestException("email already exists");

    User u = new User();
    u.setUsername(req.username());
    u.setEmail(req.email());
    u.setPasswordHash(passwordEncoder.encode(req.password()));
    return userRepository.save(u);
  }

  public User getByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalStateException("User not found: " + username));
  }

  @Transactional
  public void ensureAdminUser() {
    if (!userRepository.existsByUsername("admin")) {
      register(new RegisterRequest("admin", "admin@example.com", "123456"));
    }
  }
}
