package com.example.traffic.service;

import com.example.traffic.dto.TrafficStatusResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrafficServiceTest {

    private final TrafficService trafficService = new TrafficService();
    private final AddCarService addCarService = new AddCarService(trafficService);
    private final PassCarService passCarService = new PassCarService(trafficService);

    @Test
    void statusStartsWithRedLightAndEmptyQueue() {
        TrafficStatusResponse status = trafficService.getStatus();

        assertThat(status.greenLight()).isFalse();
        assertThat(status.queueSize()).isZero();
        assertThat(status.capacity()).isEqualTo(5);
        assertThat(status.cars()).isEmpty();
    }

    @Test
    void addCarStoresNextCarInQueue() throws InterruptedException {
        addCarService.addCar();

        TrafficStatusResponse status = trafficService.getStatus();

        assertThat(status.queueSize()).isEqualTo(1);
        assertThat(status.cars()).containsExactly("Car-1");
    }

    @Test
    void passCarReturnsNullOnRedLight() throws InterruptedException {
        addCarService.addCar();

        assertThat(passCarService.passCar()).isNull();
        assertThat(trafficService.getStatus().queueSize()).isEqualTo(1);
    }

    @Test
    void passCarRemovesNextCarOnGreenLight() throws InterruptedException {
        addCarService.addCar();
        trafficService.toggleLight();

        assertThat(passCarService.passCar()).isEqualTo("Car-1");
        assertThat(trafficService.getStatus().queueSize()).isZero();
    }
}
