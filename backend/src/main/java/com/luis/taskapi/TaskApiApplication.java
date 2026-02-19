package com.luis.taskapi;

import com.luis.taskapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TaskApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(TaskApiApplication.class, args);
  }

  @Bean
  CommandLineRunner seed(UserService userService) {
    return args -> userService.ensureAdminUser();
  }
}
