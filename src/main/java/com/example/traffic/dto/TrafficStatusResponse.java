package com.example.traffic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TrafficStatusResponse(
        @JsonProperty("isGreenLight")
        boolean greenLight,
        int queueSize,
        int capacity,
        List<String> cars
) {
}
