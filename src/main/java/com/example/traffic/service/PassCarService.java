package com.example.traffic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PassCarService {

    private final TrafficService trafficService;

    public PassCarService(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    public String passCar() throws InterruptedException {
        String car = trafficService.dequeueCar();
        if (car == null) {
            log.warn("Car consume rejected. reason=RED_LIGHT");
            return null;
        }

        log.info("Car consumed. car={}, queueSize={}", car, trafficService.getStatus().queueSize());
        return car;
    }
}
