package com.example.traffic.service;

import com.example.traffic.dto.TrafficStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class TrafficService {

    // 차량 큐의 최대 수용량
    private static final int QUEUE_CAPACITY = 5;

    // 대기 중인 차량을 보관하는 블로킹 큐
    private final BlockingQueue<String> vehicleQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    // 차량 번호를 생성하기 위한 스레드 안전한 카운터
    private final AtomicInteger carIdCounter = new AtomicInteger(1);
    // 신호등 상태 (true: 초록불, false: 빨간불). 여러 스레드에서 즉시 변경을 감지할 수 있도록 volatile 사용
    private volatile boolean greenLight;

    /**
     * 새로운 차량을 큐에 진입시킵니다.
     * 초록불일 때 큐가 꽉 찼다면 가장 오래된 차를 빼고 진입시킵니다.
     * 빨간불일 때 큐가 꽉 찼다면 빈 자리가 생길 때까지 무한 대기(Blocking)합니다.
     */
    public void enqueueCar() throws InterruptedException {
        // 초록불이면서 큐가 꽉 찬 경우 공간 확보를 위해 하나 빼냄
        if (greenLight && vehicleQueue.remainingCapacity() == 0) {
            log.info("Traffic queue is full on GREEN light. Automatically removing one car to make room.");
            vehicleQueue.poll(); // 무조건 하나 빼냄 (take는 대기하므로 poll 사용)
        } else if (!greenLight && vehicleQueue.remainingCapacity() == 0) {
            log.info("Traffic queue is full on RED light. put() will block.");
        }
        
        // 새 차량 번호 발급 및 큐에 삽입 (여유 공간이 없을 경우 대기)
        String carName = "Car-" + carIdCounter.getAndIncrement();
        vehicleQueue.put(carName); // 꽉 차면 자동으로 블로킹됨
    }

    /**
     * 큐에서 가장 먼저 들어온 차량을 빼냅니다.
     * 초록불이 아니거나 큐가 비어있는 경우 null을 반환합니다.
     */
    public String dequeueCar() throws InterruptedException {
        // 빨간불인 경우 차량이 통과할 수 없음
        if (!greenLight) {
            return null;
        }
        
        // 큐에 대기 중인 차량이 없는 경우
        if (vehicleQueue.isEmpty()) {
            return null;
        }

        // 차량 통과. 큐가 비어있으면 멈출 수 있으므로 위에서 isEmpty 검증
        return vehicleQueue.take();
    }

    /**
     * 신호등 상태를 반전(초록불 <-> 빨간불)시킵니다.
     */
    public boolean toggleLight() {
        greenLight = !greenLight;
        log.info("Traffic light toggled. greenLight={}", greenLight);
        return greenLight;
    }

    /**
     * 현재 신호등 상태 및 큐에 대기 중인 차량 정보를 반환합니다.
     */
    public TrafficStatusResponse getStatus() {
        List<String> cars = vehicleQueue.stream().toList();
        log.debug("Traffic status requested. greenLight={}, queueSize={}", greenLight, cars.size());
        return new TrafficStatusResponse(greenLight, cars.size(), QUEUE_CAPACITY, cars);
    }
}
