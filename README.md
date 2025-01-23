# 콘서트 예약 서비스 서버 구축
대기열을 사용하여 콘서트 좌석 예약을 진행하고, 예약한 내역에 대해 결제를 진행하는 서비스입니다.  동시성 제어를 통해 동시 예약이나 결제 시 중복으로 처리되지 않도록 하였습니다.

## 프로젝트 목표
- 대기열 시스템을 구축하고, 예약 서비스는 작업가능한 유저만 수행할 수 있어야 함.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용.
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 힘.

## 작업 계획 및 시나리오 분석
1. [프로젝트 Milestone](https://github.com/hwajinkim/consert-reserv-service/blob/master/docs/milestone/milestone.md)
2. [시나리오 분석](https://github.com/hwajinkim/consert-reserv-service/blob/master/docs/require-anlysis/%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD%EB%B6%84%EC%84%9D.md)
3. [ERD 설계](https://github.com/hwajinkim/consert-reserv-service/blob/master/docs/erd/ERD.md)
4. [시퀀스 다이어그램](https://github.com/hwajinkim/consert-reserv-service/blob/master/docs/sequence-diagram/sequenceDiagram.md)    
5. [API 명세서](https://github.com/hwajinkim/consert-reserv-service/blob/master/docs/API/API_%EB%AA%85%EC%84%B8.md)
 + Swagger API
![swagger_초안](https://github.com/user-attachments/assets/66324070-b637-40cd-934b-8db6993eb919)

## 동시성 제어 방식 분석
[동시성 제어 방식 분석 보고서](https://github.com/hwajinkim/concert-reserve-service/blob/feature/dev/docs/concurrency-controller/%EB%8F%99%EC%8B%9C%EC%84%B1%EC%A0%9C%EC%96%B4_%EB%B6%84%EC%84%9D_%EB%B3%B4%EA%B3%A0%EC%84%9C.md)

<!-- 6. [아키텍처 구조]()--> 

