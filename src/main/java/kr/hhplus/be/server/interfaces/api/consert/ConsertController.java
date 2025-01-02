package kr.hhplus.be.server.interfaces.api.consert;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ResponseCode;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import kr.hhplus.be.server.domain.reservation.ReservationState;
import kr.hhplus.be.server.interfaces.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConsertController {
    //1. 유저 대기열 토큰 발급 API
    @PostMapping("/queue/token")
    public ApiResponse<QueueResponse> createQueueToken(){
        QueueResponse queueResponse = new QueueResponse(
                12345L, 12345L, QueueStatus.WAIT, LocalDateTime.of(2025, 1, 1, 12, 0, 0),
                LocalDateTime.of(2025, 1, 1, 12, 30, 0));
        return ApiResponse.success(ResponseCode.TOKEN_CREATE_SUCCESS.getMessage(), queueResponse);
    }
    //2. 예약 가능 날짜 조회 API
    @GetMapping("/concerts/schedules")
    public ApiResponse<ConsertResponse> getAvailableDates(){
        ScheduleResponse scheduleResponse = new ScheduleResponse(
                12345L, LocalDateTime.of(2025, 1, 15, 19,0,0), LocalDateTime.of(2025,1,7,10,0,0),
                LocalDateTime.of(2025,1,10,18,0,0), 50);
        ScheduleResponse scheduleResponse_2 = new ScheduleResponse(
                56789L, LocalDateTime.of(2025, 1, 20,15,0,0), LocalDateTime.of(2025,1,5,10,0,0),
                LocalDateTime.of(2025,1,15,18,0,0), 30);

        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        scheduleResponses.add(scheduleResponse);
        scheduleResponses.add(scheduleResponse_2);

        ConsertResponse consertResponse = new ConsertResponse(12345L, "Awesome Concert", scheduleResponses);

        return ApiResponse.success(ResponseCode.AVAILABLE_RESERV_DATE_READ_SUCCESS.getMessage(), consertResponse);
    }

    //3. 예약 가능 좌석 조회 API
    @GetMapping("concerts/schedules/seats")
    public ApiResponse<ScheduleSeatResponse> getAvailableSeats(@RequestParam(name="scheduleId") Long scheduleId){

        SeatResponse seatResponse = new SeatResponse(1L);
        SeatResponse seatResponse_2 = new SeatResponse(2L);
        SeatResponse seatResponse_3 = new SeatResponse(3L);

        List<SeatResponse> availableSeats = new ArrayList<>();
        availableSeats.add(seatResponse);
        availableSeats.add(seatResponse_2);
        availableSeats.add(seatResponse_3);

        ScheduleSeatResponse scheduleSeatResponse = new ScheduleSeatResponse(12345L, availableSeats);

        return ApiResponse.success(ResponseCode.AVAILABLE_RESERV_SEAT_READ_SUCCESS.getMessage(), scheduleSeatResponse);
    }

    //4. 좌석 예약 API
    @PostMapping("/concerts/seats/reserve")
    public ApiResponse<ReservationResponse> createSeatReservation(@RequestBody ReservationRequest reservationRequest){

        ReservationResponse reservationResponse = new ReservationResponse(12345L, 12345L, 12345L,
                67890L, ReservationState.PANDING,
                LocalDateTime.of(2025,1,1,12,0,0, 0));
        return ApiResponse.success(ResponseCode.SEAT_RESERV_CREATE_SUCCESS.getMessage(), reservationResponse);
    }
    //5. 잔액 조회 API
    @GetMapping("/balance")
    public ApiResponse<UserBalanceResponse> getBalance(){
        UserBalanceResponse userBalanceResponse = new UserBalanceResponse(12345L, 100.00);
        return ApiResponse.success(ResponseCode.BALANCE_READ_SUCCESS.getMessage(), userBalanceResponse);
    }

    //6. 잔액 충전 API
    @PostMapping("/balance/charge")
    public ApiResponse<UserBalanceResponse> chargeBalance(@RequestBody UserBalanceRequest userBalanceRequest){

        double originalAmount = 100.00;
        double addAmount = userBalanceRequest.getAmount();
        double chargeAmount = originalAmount+addAmount;
        UserBalanceResponse userBalanceResponse = new UserBalanceResponse(12345L, chargeAmount);
        return ApiResponse.success(ResponseCode.BALANCE_CHARGE_SUCCESS.getMessage(), userBalanceResponse);
    }

    //7. 결제 API
    @PostMapping("/reservations/pay")
    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentRequeset paymentRequeset){
        PaymentResponse paymentResponse = new PaymentResponse(12345L, 12345L, 10L,
                "Awesome Concert", LocalDateTime.of(2025, 1, 1, 19,0,0),
                100.00, PaymentStatus.COMPLETED, LocalDateTime.of(2025, 1, 1, 12,0,0));
        return ApiResponse.success(ResponseCode.PAYMENT_CREATED_SUCCESS.getMessage(), paymentResponse);
    }
}
