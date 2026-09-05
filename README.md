# Traffic Service Simulation 🚦

이 프로젝트는 **Spring Boot**와 Java의 `BlockingQueue`를 활용하여 신호등 통제 하에 차량이 진입하고 통과하는 과정을 시뮬레이션하는 예제 웹 애플리케이션입니다. 동시성(Concurrency) 제어와 생산자-소비자(Producer-Consumer) 패턴을 시각적으로 학습하고 테스트할 수 있도록 설계되었습니다.

## 🚀 주요 기능

- **차량 진입 (Produce)**:
  - 새로운 차량을 대기열(Queue)에 추가합니다.
  - 대기열 공간(최대 5대)이 가득 찬 상태에서:
    - **초록불**일 경우: 가장 오래된 차량을 자동으로 내보내고 진입합니다.
    - **빨간불**일 경우: 앞차가 빠져나갈 때까지 스레드가 대기(Blocking)합니다.
- **차량 통과 (Consume)**:
  - 신호등이 초록불일 때 큐의 맨 앞 차량이 대기열을 통과합니다.
  - 신호등이 빨간불이거나 대기열이 비어있으면 차량이 통과할 수 없습니다.
- **신호등 제어 (Toggle Light)**:
  - 초록불과 빨간불 상태를 전환합니다.
- **실시간 웹 UI**:
  - `index.html`을 통해 실시간으로 큐의 상태와 신호등 색상을 직관적으로 확인할 수 있습니다.
  - 비동기 `fetch` API 및 폴링(Polling)을 사용하여 서버 상태를 지속적으로 렌더링합니다.

## 🛠 기술 스택

- **Backend**: Java 21, Spring Boot 3.4.1, `java.util.concurrent.ArrayBlockingQueue`
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Build Tool**: Gradle 9.6.1

## 📁 핵심 클래스 구조

- `TrafficService`: `ArrayBlockingQueue`를 활용하여 차량 진입(`enqueueCar`), 방출(`dequeueCar`), 신호등 제어 로직 등 핵심 동시성 제어를 담당합니다.
- `ProduceController` & `ConsumeController` & `TrafficController`: 프론트엔드와 통신하기 위한 REST API 엔드포인트를 제공합니다.
- `AddCarService` & `PassCarService`: 컨트롤러와 서비스 간의 비즈니스 레이어 역할을 담당합니다.

## 🏃 실행 방법

1. 저장소를 클론하거나 프로젝트 폴더로 이동합니다.
2. Gradle Wrapper를 사용하여 애플리케이션을 실행합니다:
   ```bash
   ./gradlew bootRun
   ```
3. 웹 브라우저에서 아래 주소로 접속합니다:
   ```text
   http://localhost:8080
   ```

## 💡 동시성(Concurrency) 학습 포인트

이 애플리케이션은 실무에서 발생할 수 있는 스레드 제어 이슈를 예방하기 위해 다음과 같이 설계되었습니다.
- **Thread-Safety**: `synchronized` 키워드 남용으로 인한 데드락(Deadlock)을 방지하고 `BlockingQueue` 내장 기능을 적극 활용했습니다.
- **Non-blocking UI**: 백엔드 스레드가 대기(Blocking) 상태에 빠지더라도 웹 UI는 멈추지 않도록 설계되었습니다.
