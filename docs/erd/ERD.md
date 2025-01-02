# ERD 작성
```mermaid
---
config:
  theme: default
---
erDiagram
    USER {
        UUID UserId PK "Primary Key, 사용자 ID"
        VARCHAR UserName "사용자 이름"
        DECIMAL PointsBalance "포인트 잔액"
        DATETIME CreatedAt "생성 시각"
        DATETIME UpdatedAt "수정 시각"
    }
    QUEUE {
        BIGINT QueueId PK "Primary Key, 대기열 ID"
        UUID UserId FK "Foreign Key, 사용자 ID"
        VARCHAR QueueStatus "대기열 상태"
        DATETIME TokenCreatedAt "토큰 생성 시각"
        DATETIME TokenExpiredAt "토큰 만료 시각"
        DATETIME TokenRemovedAt "토큰 제거 시각"
    }
    CONCERT {
        BIGINT ConcertId PK "Primary Key, 콘서트 ID"
        VARCHAR ConcertName "콘서트 이름"
        DATETIME CreatedAt "생성 시각"
        DATETIME UpdatedAt "수정 시각"
    }
    SCHEDULE {
        BIGINT ScheduleId PK "Primary Key, 스케줄 ID"
        BIGINT ConcertId FK "Foreign Key, 콘서트 ID"
        DECIMAL Price "가격"
        DATETIME ConcertDate "콘서트 일자"
        DATETIME BookingStart "예약 가능 시작 시간"
        DATETIME BookingEnd "예약 종료 시간"
        INT RemainingTickets "잔여 티켓 수"
        DATETIME CreatedAt "생성 시각"
        DATETIME UpdatedAt "수정 시각"
    }
    SEAT {
        BIGINT SeatId PK "Primary Key, 좌석 ID"
        BIGINT ScheduleId FK "Foreign Key, 스케줄 ID"
        VARCHAR SeatNumber "좌석 번호"
        ENUM SeatStatus "좌석 상태 (OCCUPIED/AVAILABLE)"
        DECIMAL SeatPrice "좌석 가격"
        DATETIME CreatedAt "생성 시각"
        DATETIME UpdatedAt "수정 시각"
    }
    RESERVATION {
        BIGINT ReservationId PK "Primary Key, 예약 ID"
        UUID UserId FK "Foreign Key, 사용자 ID"
        BIGINT SeatId FK "Foreign Key, 좌석 ID"
        ENUM ReservationStatus "예약 상태 (PENDING/PAID/CANCELLED)"
        DECIMAL SeatPrice "좌석 가격"
        DATETIME CreatedAt "생성 시각"
    }
    PAYMENT {
        BIGINT PaymentId PK "Primary Key, 결제 ID"
        BIGINT ReservationId FK "Foreign Key, 예약 ID"
        VARCHAR SeatNumber "좌석 번호"
        VARCHAR ConcertName "콘서트 이름"
        DATETIME ConcertDateTime "콘서트 일시"
        DECIMAL PaymentAmount "결제 금액"
        ENUM PaymentStatus "결제 상태 (COMPLETED/FAILED/CANCELLED)"
        DATETIME PaymentTime "결제 시각"
        DATETIME CreatedAt "생성 시각"
    }
    USER ||--o{ QUEUE : "1:N"
    CONCERT ||--o{ SCHEDULE : "1:N"
    SCHEDULE ||--o{ SEAT : "1:N"
    SEAT ||--o{ RESERVATION : "1:N"
    RESERVATION ||--o| PAYMENT : "1:1"
    USER ||--o{ RESERVATION : "1:N"
```

# 테이블 간 관계 설명
## 1. 사용자 테이블  ↔ 대기열 테이블
    - 1:N 관계
    - 하나의 사용자는 여러 대기열 기록을 가질 수 있음.
      
## 2. 콘서트 테이블 ↔ 콘서트 스케줄 테이블 
    - 1:N 관계
    - 하나의 콘서트는 여러 개의 스케줄을 가질 수 있음.
    
## 3. 콘서트 스케줄 테이블 ↔ 좌석 테이블
    - 1:N 관계
    - 하나의 콘서트 스케줄에 여러 좌석이 배정됨.
    
## 4. 좌석 테이블 ↔ 예약 테이블 
    - 1:N 관계
    - 하나의 좌석은 여러 예약 상태를 가질 수 있으나, 동시에 점유될 수 없음.
    
## 5. 사용자 테이블 ↔ 예약 테이블 
    - 1:N 관계
    - 하나의 사용자는 여러 예약을 생성할 수 있음.
    
## 6. 예약 테이블 ↔ 결제 테이블 
    - 1:1 관계
    - 하나의 예약은 하나의 결제만 가질 수 있음.
    
## 7. 결제 테이블 ↔ 좌석 테이블 
    - 좌석 번호(`SeatNumber`)와 콘서트 관련 정보는 결제 기록에 저장되지만, 직접 참조되지는 않음.

