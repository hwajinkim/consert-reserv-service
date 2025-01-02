package kr.hhplus.be.server.interfaces.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ConsertResponse {
    private Long consertId;
    private String consertName;
    private List<ScheduleResponse> scheduleResponses;
}
