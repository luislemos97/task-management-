package com.luis.taskapi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private TaskStatus status = TaskStatus.TODO;

  private LocalDate dueDate;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @PrePersist
  void prePersist() {
    this.createdAt = LocalDateTime.now();
    if (this.status == null) this.status = TaskStatus.TODO;
  }

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public TaskStatus getStatus() { return status; }
  public void setStatus(TaskStatus status) { this.status = status; }
  public LocalDate getDueDate() { return dueDate; }
  public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
}
