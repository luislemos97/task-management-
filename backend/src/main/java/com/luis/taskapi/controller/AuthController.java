package com.luis.taskapi.controller;

import com.luis.taskapi.dto.LoginRequest;
import com.luis.taskapi.dto.RegisterRequest;
import com.luis.taskapi.dto.TokenResponse;
import com.luis.taskapi.security.JwtService;
import com.luis.taskapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserService userService;

  public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
    var user = userService.register(req);
    var body = new LinkedHashMap<String, Object>();
    body.put("id", user.getId());
    body.put("username", user.getUsername());
    body.put("email", user.getEmail());
    body.put("createdAt", user.getCreatedAt());
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
      String token = jwtService.generateToken(req.username());
      return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
    } catch (AuthenticationException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("", "Bearer"));
    }
  }
}
