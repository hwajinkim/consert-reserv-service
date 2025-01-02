package kr.hhplus.be.server.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ResponseCode {

    TOKEN_CREATE_SUCCESS(HttpStatus.CREATED, true, "유저 대기열 토큰 발급에 성공했습니다."),
    AVAILABLE_RESERV_DATE_READ_SUCCESS(HttpStatus.OK, true, "예약 가능 날짜 조회에 성공했습니다."),
    AVAILABLE_RESERV_SEAT_READ_SUCCESS(HttpStatus.OK, true, "예약 가능 좌석 조회에 성공했습니다."),
    SEAT_RESERV_CREATE_SUCCESS(HttpStatus.CREATED, true, "좌석 예약에 성공했습니다."),
    BALANCE_READ_SUCCESS(HttpStatus.OK, true, "잔액 조회에 성공했습니다."),
    BALANCE_CHARGE_SUCCESS(HttpStatus.OK, true, "잔액 충전에 성공했습니다."),
    PAYMENT_CREATED_SUCCESS(HttpStatus.CREATED, true, "결제에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
