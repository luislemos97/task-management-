package com.luis.taskapi.dto;

import com.luis.taskapi.entity.TaskStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record TaskRequest(
    @NotBlank @Size(min = 3, max = 100) String title,
    @Size(max = 500) String description,
    @NotNull TaskStatus status,
    LocalDate dueDate
) {}
