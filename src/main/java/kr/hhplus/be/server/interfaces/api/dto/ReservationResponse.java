package kr.hhplus.be.server.interfaces.api.dto;

import kr.hhplus.be.server.domain.reservation.ReservationState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReservationResponse {
    private Long reservationId;
    private Long scheduleId;
    private Long seatId;
    private Long userId;
    private ReservationState state;
    private LocalDateTime createAt;
}
