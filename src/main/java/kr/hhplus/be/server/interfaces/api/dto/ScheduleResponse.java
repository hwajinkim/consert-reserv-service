package kr.hhplus.be.server.interfaces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleResponse {
    private Long scheduleId;
    private LocalDateTime concertDateTime;
    private LocalDateTime reservationStartTime;
    private LocalDateTime reservationEndTime;
    private int remainingTickets;
}
