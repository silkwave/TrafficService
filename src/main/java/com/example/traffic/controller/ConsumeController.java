package com.example.traffic.controller;

import com.example.traffic.dto.ConsumeResponse;
import com.example.traffic.service.PassCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/traffic")
@Slf4j
public class ConsumeController {

    private final PassCarService passCarService;

    public ConsumeController(PassCarService passCarService) {
        this.passCarService = passCarService;
    }

    @PostMapping("/consume")
    public ConsumeResponse consumeCar() {
        try {
            String car = passCarService.passCar();
            if (car == null) {
                return new ConsumeResponse(false, null, "통과 실패 (Red Light)");
            }

            return new ConsumeResponse(true, car, "차량 통과 완료");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Car consume interrupted", e);
            return new ConsumeResponse(false, null, "차량 통과 중단 (Interrupted)");
        }
    }
}
