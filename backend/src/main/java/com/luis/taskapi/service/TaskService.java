package com.luis.taskapi.service;

import com.luis.taskapi.dto.TaskRequest;
import com.luis.taskapi.dto.TaskResponse;
import com.luis.taskapi.entity.Task;
import com.luis.taskapi.entity.TaskStatus;
import com.luis.taskapi.error.ResourceNotFoundException;
import com.luis.taskapi.repository.TaskRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TaskService {
  private final TaskRepository taskRepository;
  private final UserService userService;

  public TaskService(TaskRepository taskRepository, UserService userService) {
    this.taskRepository = taskRepository;
    this.userService = userService;
  }

  @Transactional
  public TaskResponse create(String username, TaskRequest req) {
    var user = userService.getByUsername(username);
    Task t = new Task();
    apply(t, req);
    t.setUser(user);
    return toResponse(taskRepository.save(t));
  }

  public Page<TaskResponse> list(String username, TaskStatus status, int page, int size) {
    var user = userService.getByUsername(username);
    Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<Task> tasks = (status == null)
        ? taskRepository.findAllByUserId(user.getId(), pageable)
        : taskRepository.findAllByUserIdAndStatus(user.getId(), status, pageable);
    return tasks.map(this::toResponse);
  }

  public TaskResponse get(String username, UUID id) {
    var user = userService.getByUsername(username);
    Task t = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    if (!t.getUser().getId().equals(user.getId())) throw new ResourceNotFoundException("Task not found: " + id);
    return toResponse(t);
  }

  @Transactional
  public TaskResponse update(String username, UUID id, TaskRequest req) {
    var user = userService.getByUsername(username);
    Task t = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    if (!t.getUser().getId().equals(user.getId())) throw new ResourceNotFoundException("Task not found: " + id);
    apply(t, req);
    return toResponse(taskRepository.save(t));
  }

  @Transactional
  public void delete(String username, UUID id) {
    var user = userService.getByUsername(username);
    Task t = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    if (!t.getUser().getId().equals(user.getId())) throw new ResourceNotFoundException("Task not found: " + id);
    taskRepository.delete(t);
  }

  private void apply(Task t, TaskRequest req) {
    t.setTitle(req.title());
    t.setDescription(req.description());
    t.setStatus(req.status());
    t.setDueDate(req.dueDate());
  }

  private TaskResponse toResponse(Task t) {
    return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getDueDate(), t.getCreatedAt());
  }
}
