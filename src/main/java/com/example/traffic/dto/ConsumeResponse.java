package com.example.traffic.dto;

public record ConsumeResponse(
        boolean success,
        String car,
        String message
) {
}
