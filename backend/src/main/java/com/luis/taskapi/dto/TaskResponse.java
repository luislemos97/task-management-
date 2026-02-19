package com.luis.taskapi.dto;

import com.luis.taskapi.entity.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
    UUID id,
    String title,
    String description,
    TaskStatus status,
    LocalDate dueDate,
    LocalDateTime createdAt
) {}
