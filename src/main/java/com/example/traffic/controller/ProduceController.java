package com.example.traffic.controller;

import com.example.traffic.dto.ActionResponse;
import com.example.traffic.service.AddCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/traffic")
@Slf4j
public class ProduceController {

    private final AddCarService addCarService;

    public ProduceController(AddCarService addCarService) {
        this.addCarService = addCarService;
    }

    @PostMapping("/produce")
    public ActionResponse produceCar() {
        try {
            addCarService.addCar();
            return new ActionResponse(true, "차량 진입 완료");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Car produce interrupted", e);
            return new ActionResponse(false, "차량 진입 중단 (Interrupted)");
        }
    }
}
