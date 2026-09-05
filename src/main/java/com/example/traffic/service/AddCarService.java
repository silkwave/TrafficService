package com.example.traffic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddCarService {

    private final TrafficService trafficService;

    public AddCarService(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    public void addCar() throws InterruptedException {
        trafficService.enqueueCar();
        log.info("Car produced. queueSize={}", trafficService.getStatus().queueSize());
    }
}
