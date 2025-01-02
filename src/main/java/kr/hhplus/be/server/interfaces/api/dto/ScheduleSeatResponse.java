package kr.hhplus.be.server.interfaces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleSeatResponse {
    private Long scheduleId;
    private List<SeatResponse> avaliableSeats;
}
