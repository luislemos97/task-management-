package com.luis.taskapi.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(err -> err.getField() + " " + err.getDefaultMessage())
        .orElse("Validation error");
    ApiError body = new ApiError(LocalDateTime.now(), 400, "Bad Request", msg, req.getRequestURI());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
    ApiError body = new ApiError(LocalDateTime.now(), 400, "Bad Request", "Invalid parameter: " + ex.getName(), req.getRequestURI());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
    ApiError body = new ApiError(LocalDateTime.now(), 404, "Not Found", ex.getMessage(), req.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
    ApiError body = new ApiError(LocalDateTime.now(), 400, "Bad Request", ex.getMessage(), req.getRequestURI());
    return ResponseEntity.badRequest().body(body);
  }
}
