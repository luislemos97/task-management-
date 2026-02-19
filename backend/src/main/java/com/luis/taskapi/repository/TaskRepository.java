package com.luis.taskapi.repository;

import com.luis.taskapi.entity.Task;
import com.luis.taskapi.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
  Page<Task> findAllByUserId(UUID userId, Pageable pageable);
  Page<Task> findAllByUserIdAndStatus(UUID userId, TaskStatus status, Pageable pageable);
}
