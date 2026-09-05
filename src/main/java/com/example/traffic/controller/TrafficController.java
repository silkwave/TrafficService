package com.example.traffic.controller;

import com.example.traffic.dto.LightResponse;
import com.example.traffic.dto.TrafficStatusResponse;
import com.example.traffic.service.TrafficService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {

    private final TrafficService trafficService;

    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @GetMapping("/status")
    public TrafficStatusResponse getStatus() {
        return trafficService.getStatus();
    }

    @PostMapping("/toggle-light")
    public LightResponse toggleLight() {
        return new LightResponse(trafficService.toggleLight());
    }
}
