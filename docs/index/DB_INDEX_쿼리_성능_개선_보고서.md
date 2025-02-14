# DB Index 쿼리 성능 개선 보고서

**목차**
1. 개요<br>
  1.1 인덱스란?<br>
  1.2 인덱스 최적화 필요성<br>
  1.3 인덱스 사용 시 주의사항<br>
2. 주요 시나리오 및 쿼리 분석<br>
  2.1 자주 사용되는 주요 쿼리 목록 및 현황 분석<br>
3. 인덱스 설계 및 적용<br>
  3.1 인덱스 추가 및 적용 내용<br>
4.  성능 비교 및 개선 결과<br>
  4.1 인덱스 추가 전후 성능 비교 (EXPLAIN, 실행 시간 등)<br>
5. 결론<br>

## 1. 개요
### 1.1 인덱스란?
인덱스는 데이터베이스 테이블에 대한 검색 성능의 속도를 높여주는 자료구조이다. 특정 컬럼에 인덱스를 생성하면, 해당 컬럼의 데이터들을 정렬하여 별도의 메모리 공간에 데이터의 물리적 주소와 함께 저장된다.
또한, 인덱스 생성 시 오름차순으로 정렬하기 때문에 정렬된 주소 체계라고 표현할 수 있다.

### 1.2 인덱스 최적화 필요성
- **빠른 데이터 조회**: 인덱스가 없거나 잘못 설계된 경우, 쿼리를 실행할 때 **Full Table Scan**이 발생하여 모든 데이터를 검색해야 하는데 적절한 인덱스를 설정하면 필요한 데이터만 빠르게 조회가 가능하다.
- **조인 성능 개선**: 여러 개의 테이블을 조인할 때, 인덱스가 없으면 각 테이블을 풀 스캔해야 하므로 성능이 크게 저하되는데 **외래 키(Foreign Key) 컬럼**에 인덱스를 추가하면 조인 연산 시 검색 속도가 대폭 향상된다.
- **정렬 및 그룹화 최적화 :** ORDER BY, GROUP BY가 포함된 쿼리는 테이블을 정렬해야 하므로 성능 저하를 발생시키는데 정렬이 자주 필요한 컬럼에 인덱스를 추가하면 불필요한 정렬 연산을 줄일 수 있다.

### 1.3 인덱스 사용 시 주의사항
- 쓰기(INSERT, UPDATE, DELETE) 성능 저하
    - 인덱스가 많을수록 데이터를 추가하거나 변경할 때 인덱스도 함께 갱신해야 하므로 성능이 저하된다.
- 저장 공간 증가
    - 인덱스를 추가하면 별도의 **저장 공간이 필요**하며, 데이터베이스의 크기가 증가할 수 있다.
- 과도한 인덱스 사용은 오히려 성능 저하
    - 인덱스 관리를 위해 DB의 약 10%의 저장공간이 필요한데, 너무 많은 인덱스를 추가하면 데이터베이스가 인덱스를 관리하는 오버헤드(부하)가 증가할 수 있음.

## 2. 주요 시나리오 및 쿼리 분석
### 2.1 자주 사용되는 주요 쿼리 목록 및 현황 분석
- 예약 가능 날짜 조회
  ```
  SELECT c.*, s.* 
  FROM Concert c 
  JOIN Schedule s 
  ON c.concert_id = s.concert_id 
  WHERE c.concert_id = 1;
  ```
  - 쿼리 분석
      - Schedule 테이블에서 특정 공연(concert_id)의 날짜를 조회한다.
  - 성능 문제
      - JOIN 연산 비용 : Concert과 Schedule을 JOIN 하기 때문에 스케줄이 많은 콘서트에서는 성능이 저하될 수 있다.
      - Index 미적용 시 성능 저하 : WHERE c.id = :concertId에서 적절한 인덱스가 없으면 풀 테이블 스캔(Full Table Scan)이 발생할 가능성이 있다.

- 예약 가능 좌석 조회
  ```
  SELECT c.*, s.*
  FROM Schedule c
  JOIN Seat s ON c.schedule_id = s.schedule_id
  WHERE c.schedule_id = 1
  AND s.seat_status = 'AVAILABLE';
  ```
  - 쿼리 분석
      - 특정 scheduleId에 해당하는 Schedule을 조회하면서, 해당 Schedule에 속한 예약 가능(AVAILABLE) 좌석도 함께 가져온다.
  - 성능 문제
      - seatStatus 조건 필터링이 인덱스로 최적화되지 않으면 성능 저하 발생 가능하다.
      
## 3. 인덱스 설계 및 적용
### 3.1 인덱스 추가 및 적용 내용
1. Concert 테이블 인덱스
    
    ```sql
    CREATE INDEX idx_concert_id ON Concert (concert_id);
    ```
    
    - Concert 테이블의 id 컬럼에 인덱스를 생성하여 WHERE c.id = :concertId 조건을 빠르게 검색할 수 있도록 한다.
2. Schedule 테이블 인덱스 
    
    ```sql
    CREATE INDEX idx_schedule_concert_id ON Schedule (concert_id);
    CREATE INDEX idx_schedule_id ON Schedule (schedule_id);
    ```
    
    - Schedule 테이블에서 concert_id를 기준으로 Concert와 JOIN이 발생하므로, concert_id에 대한 인덱스를 추가한다.
3.  Seat 테이블 인덱스 
    
    ```sql
    CREATE INDEX idx_seat_schedule_id_status ON Seat (schedule_id, seat_status);
    ```
    
    - Seat 테이블에서 schedule_id와 seatStatus = 'AVAILABLE' 조건을 함께 검색하므로 복합 인덱스를 적용한다.
    - schedule_id로 필터링 후 seatStatus를 빠르게 찾을 수 있다.

## 4.  성능 비교 및 개선 결과<br>
### 4.1 인덱스 추가 전후 성능 비교 (EXPLAIN, 실행 시간 등)
Concert : 100건, Schedule : 10만 건, Seat: 100만 건
- **예약 가능 날짜 조회**
    
    ```sql
    SELECT c.*, s.* FROM Concert c JOIN Schedule s ON c.concert_id = s.concert_id WHERE c.concert_id = 1;
    ```
    
    - 인덱스 추가 전
      <img width="1434" alt="스크린샷 2025-02-14 오전 6 37 50" src="https://github.com/user-attachments/assets/c30e35a1-9156-491c-8943-91ab4f5d0273" />
      <img width="1444" alt="스크린샷 2025-02-14 오전 6 39 14" src="https://github.com/user-attachments/assets/c36b3ba6-0e39-4873-b790-2966bb6ac1b1" />
    - Explain 결과
        - 실행 row 수 : 10만
        - 실행 시간 : 700ms
    
    - 인덱스 추가 후
      <img width="1485" alt="스크린샷 2025-02-14 오전 7 23 55" src="https://github.com/user-attachments/assets/8fe0f7af-a62c-42c8-97d1-32717c835b8f" />
      <img width="1563" alt="스크린샷 2025-02-14 오전 7 24 12" src="https://github.com/user-attachments/assets/ce656c5d-8323-4a02-b491-8388394aa83b" />

    - Explain 결과
        - 실행 row 수 : 10만
        - 실행 시간 : 500ms
   
    - 전 & 후 실행 시간을 비교해본 결과 700ms → 500ms로 성능이 약 **28.57%** 개선되었다.
      
- **예약 가능 좌석 조회**
    
    ```sql
    SELECT c.*, s.* FROM Schedule c JOIN Seat s ON c.schedule_id = s.schedule_id WHERE c.schedule_id = 1 AND s.seat_status = 'AVAILABLE';
    ```    
    - 인덱스 추가 전
        <img width="1573" alt="스크린샷 2025-02-14 오전 8 08 17" src="https://github.com/user-attachments/assets/4f6426bf-9a69-489e-bd76-61ceabe6597e" />
        - Explain 결과
            - 실행 row 수 : 500,609건
            - 실행 시간 : 3060ms
    - 인덱스 추가 후
        <img width="1574" alt="스크린샷 2025-02-14 오전 8 07 45" src="https://github.com/user-attachments/assets/435423b9-9541-4282-8893-1e4485dd53fc" />
        - Explain 결과
            - 실행 row 수 : 500,609건
            - 실행 시간 : 1730ms
    
    - 전 & 후 실행 시간을 비교해본 결과 3060ms → 1730ms로 성능이 약 **43.46%** 개선되었다.
      
## 결론
- schedule_id, concert_id와 같이 특정 컬럼만 조회할 경우 단일 인덱스가 효과적이다.
- 복합 인덱스(schedule_id, seat_status)는 두 조건을 동시에 처리하여 성능을 향상시키고, 단일 인덱스 두 개보다 하나의 복합 인덱스가 더 최적화된 검색을 제공한다.
- schedule_id, seat_status 순서로 인덱스를 만들었을 경우, schedule_id가 먼저 조건에 포함될 때만 효율적으로 동작한다.
