package com.example.traffic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LightResponse(
        @JsonProperty("isGreenLight")
        boolean greenLight
) {
}
