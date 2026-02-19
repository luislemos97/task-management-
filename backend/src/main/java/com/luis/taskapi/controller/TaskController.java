package com.luis.taskapi.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.luis.taskapi.dto.TaskRequest;
import com.luis.taskapi.dto.TaskResponse;
import com.luis.taskapi.entity.TaskStatus;
import com.luis.taskapi.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public ResponseEntity<TaskResponse> create(Authentication auth, @Valid @RequestBody TaskRequest req) {
    return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(auth.getName(), req));
  }

  @GetMapping
  public Page<TaskResponse> list(Authentication auth,
                                @RequestParam(required = false) TaskStatus status,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
    return taskService.list(auth.getName(), status, page, size);
  }

  @GetMapping("/{id}")
  public TaskResponse get(Authentication auth, @PathVariable UUID id) {
    return taskService.get(auth.getName(), id);
  }

  @PutMapping("/{id}")
  public TaskResponse update(Authentication auth, @PathVariable UUID id, @Valid @RequestBody TaskRequest req) {
    return taskService.update(auth.getName(), id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(Authentication auth, @PathVariable UUID id) {
    taskService.delete(auth.getName(), id);
  }
}
