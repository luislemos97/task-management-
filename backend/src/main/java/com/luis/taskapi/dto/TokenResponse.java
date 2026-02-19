package com.luis.taskapi.dto;

public record TokenResponse(
    String token,
    String tokenType
) {}
