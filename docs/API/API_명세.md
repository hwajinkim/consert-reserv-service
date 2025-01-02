# API 명세

## `POST` /api/v1/queue/token - 유저 대기열 토큰 발급 API
* 개요 : 헤더에 유저 인증 토큰이 없으면 유저마다 대기열 토큰을 생성하고 관련 정보를 저장한다.
  
### Request
**Headers**

| Key | Value | Description |
| --- | --- | --- |
| `Authorization` | `Bearer <token>` | 유저 인증 토큰 |

### Response
**Success (201 Created)**
```json
{
  "statusCode": 201,
  "success" : "true",
  "message": "유저 대기열 토큰 발급 성공",
  "data": {
	  "queueId": "1a2b3c4d5e6f7g8h9i0j",
	  "userId": "12345",
	  "queueStatus": "WAIT",
	  "tokenCreatedAt": "2025-01-01T12:00:00Z",
	  "tokenExpiresAt": "2025-01-01T12:30:00Z"
  }
}
```
### Error Responses
**401 Unauthorized**
```json
{
  "statusCode": 401,
  "success" : "false",
  "message": "인증 토큰이 유효하지 않습니다."
}
```
**500 Internal Server Error**
```json
{
  "statusCode": 500,
  "success" : "false",
  "message": "서버 오류가 발생했습니다."
}
```

## `GET` /api/v1/concerts/schedules - 예약 가능 날짜 조회 API
* 개요 : 예약 가능한 날짜 목록을 조회하여 반환한다.

### Request
**Headers**
| Key | Value | Description |
| --- | --- | --- |
| `Authorization` | `Bearer <token>` | 유저 인증 토큰 |

### Response
**Success (200 OK)**
```json
{
  "statusCode": 200,
  "success" : "true",
  "message": "예약 가능 날짜 조회 성공",
  "data": { 
    [
      {
        "concertId": "12345",
        "concertName": "Awesome Concert",
        "schedules": [
          {
              "scheduleId": "67890",
              "concertDate": "2025-01-15",
              "reservationStartTime": "2025-01-01T10:00:00Z",
              "reservationEndTime": "2025-01-10T18:00:00Z",
              "remainingTickets": 50
          },
          {
              "scheduleId": "67891",
              "concertDate": "2025-01-20",
              "reservationStartTime": "2025-01-05T10:00:00Z",
              "reservationEndTime": "2025-01-15T18:00:00Z",
              "remainingTickets": 30
          }
        ]
      }
    ]	  
  }
}
```
### Error Responses
**404 Not Found**
```json
{
  "statusCode": 404,
  "success" : "false",
  "message": "예약 가능한 날짜가 없습니다."
}
```
**401 Unauthorized**
```json
{
  "statusCode": 401,
  "success" : "false",
  "message": "인증 토큰이 유효하지 않습니다."
}
```
**500 Internal Server Error**
```json
{
  "statusCode": 500,
  "success" : "false",
  "message": "서버 오류가 발생했습니다."
}
```

## `GET` /api/v1/concerts/schedules/seats - 예약 가능 좌석 조회 API
* 개요 : 특정 날짜의 예약 가능한 좌석 정보를 조회하여 반환한다.
### Request
**Headers**
| Key | Value | Description |
| --- | --- | --- |
| `Authorization` | `Bearer <token>` | 유저 인증 토큰 |

**Query Parameters**
| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `scheduleId` | `string` | `true` | 특정 스케줄 ID |

**Example Request**
```
GET /api/v1/concerts/schedules/seats?scheduleId=12345
```

### Response
**Success (200 OK)**
```json
{
  "statusCode": 200,
  "success" : "true",
  "message": "예약 가능 좌석 조회 성공",
  "data": { 
    "scheduleId": "12345",
    "availableSeats": [
      {
        "seatId": "1",
      },
      {
        "seatId": "2",
      },
      {
        "seatId": "3",
      }
    ]
  }
}
```

### Error Responses
**404 Not Found**
```json
{
  "statusCode": 404,
  "success" : "false",
  "message": "예약 가능한 좌석이 없습니다."
}
```
**401 Unauthorized**
```json
{
  "statusCode": 401,
  "success" : "false",
  "message": "인증 토큰이 유효하지 않습니다."
}
```
**500 Internal Server Error**
```json
{
  "statusCode": 500,
  "success" : "false",
  "message": "서버 오류가 발생했습니다."
}
```

## `POST` /api/v1/concerts/seats/reserve - 좌석 예약 API
* 개요 : 스케줄 정보와 좌석 정보로 좌석 데이터를 조회한 후 좌석 예약을 처리한다.

### Request
**Headers**
| Key | Value | Description |
| --- | --- | --- |
| `Authorization` | `Bearer <token>` | 유저 인증 토큰 |

**Body**
| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `scheduleId` | `string` | `true` | 예약할 스케줄 ID |
| `seatId` | `string` | `true` | 예약할 좌석 ID |

**Example Request**
```json
{
  "scheduleId": "12345",
  "seatId": "10"
}
```

### Response
**Success (201 Created)**
```json
{
  "statusCode": 201,
  "success" : "true",
  "message": "좌석 예약 성공",
  "data": {
	  "reservationId": "12345",
	  "scheduleId": "12345",
	  "seatId": "10",
	  "userId": "67890",
	  "status": "PENDING",
	  "createdAt": "2025-01-01T12:00:00Z"
	}	
}
```
### Error Responses
**404 Not Found**
```json
{
  "statusCode": 404,
  "success" : "false",
  "message": "해당 스케줄ID와 좌석 ID로 좌석 데이터를 찾을 수 없습니다."
}
```
**401 Unauthorized**
```json
{
  "statusCode": 401,
  "success" : "false",
  "message": "인증 토큰이 유효하지 않습니다."
}
```
**500 Internal Server Error**
```json
{
  "statusCode": 500,
  "success" : "false",
  "message": "서버 오류가 발생했습니다."
}
```

## `GET` /api/v1/balance - 잔액 조회 API
* 개요 : 유저 토큰을 통해 사용자 정보를 확인하고, 해당 사용자의 현재 잔액을 조회한다.

### Request
**Headers**
| Key | Value | Description |
| --- | --- | --- |
| `Authorization` | `Bearer <token>` | 유저 인증 토큰 |

### Response
**Success (200 OK)**
```json
{
  "statusCode": 200,
  "success" : "true",
  "message": "잔액 조회 성공",
  "data": {
	  "userId": "12345",
	  "balance": 100.00
	}
}
```
### Error Responses
**404 Not Found**
```json
{
  "statusCode": 404,
  "success" : "false",
  "message": "사용자 정보를 찾을 수 없습니다."
}
```
**401 Unauthorized**
```json
{
  "statusCode": 401,
  "success" : "false",
  "message": "인증 토큰이 유효하지 않습니다."
}
```
**500 Internal Server Error**
```json
{
  "statusCode": 500,
  "success" : "false",
  "message": "서버 오류가 발생했습니다."
}
```

## `POST` /api/v1/balance/charge - 잔액 충전 API
* 개요 : 유저 토큰을 통해 사용자 정보를 확인하고, 요청된 금액만큼 잔액을 충전한다.

### Request
**Headers**
| Key | Value | Description |
| --- | --- | --- |
| `Authorization` | `Bearer <token>` | 유저 인증 토큰 |

**Body**
| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `amount` | `number` | `true` | 충전할 금액 |

**Example Request**
```json
{
  "amount": 50.00
}
```

### Response
**Success (200 OK)**
```json
{
  "statusCode": 200,
  "success" : "true",
  "message": "잔액 충전 성공",
  "data": {
    "userId": "12345",
    "balance": 150.00
  }
}
```

### Error Responses
**400 Bad Request**
```json
{
  "statusCode": 400,
  "success" : "false",
  "message": "충전 금액이 유효하지 않습니다."
}
```
**401 Unauthorized**
```json
{
  "statusCode": 401,
  "success" : "false",
  "message": "인증 토큰이 유효하지 않습니다."
}
```
**500 Internal Server Error**
```json
{
  "statusCode": 500,
  "success" : "false",
  "message": "서버 오류가 발생했습니다."
}
```

### `POST` /api/v1/reservations/pay - 결제 API
개요 : 특정 예약 정보를 조회한 후 결제를 처리하고 결제 내역을 생성한다.

### Request
**Headers**
| Key | Value | Description |
| --- | --- | --- |
| `Authorization` | `Bearer <token>` | 유저 인증 토큰 |

**Body**
| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `reservationId` | `string` | `true` | 예약 ID |
| `seatId` | `string` | `true` | 좌석 ID |

**Example Request**
```json
{
  "reservationId": "12345",
  "seatId": "10"
}
```

### **Response**

**Success (200 OK)**

```json
{
  "statusCode": 200,
  "success" : "true",
  "message": "결제 성공",
  "data": {
	  "paymentId": "12345",
	  "reservationId": "12345",
	  "seatId": "10",
	  "concertName": "Awesome Concert",
	  "concertDateTime": "2025-01-01T19:00:00Z",
	  "amount": 100.00,
	  "paymentStatus": "COMPLETED",
	  "paymentTime": "2025-01-01T12:00:00Z"
	}
}
```

### Error Responses
**404 Not Found**
```json
{
  "statusCode": 404,
  "success" : "false",
  "message": "예약 정보를 찾을 수 없습니다."
}
```
```json
{
  "statusCode": 404,
  "success" : "false",
  "message": "좌석 정보를 찾을 수 없습니다."
}
```

**401 Unauthorized**
```json
{
  "statusCode": 401,
  "success" : "false",
  "message": "인증 토큰이 유효하지 않습니다."
}
```

**400 Bad Request**
```json
{
  "statusCode": 400,
  "success" : "false",
  "message": "잔액이 결제 금액보다 적습니다."
}
```

**500 Internal Server Error**
```json
{
  "statusCode": 500,
  "success" : "false",
  "message": "서버 오류가 발생했습니다."
}
```
