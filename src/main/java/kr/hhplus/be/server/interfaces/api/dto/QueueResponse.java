package kr.hhplus.be.server.interfaces.api.dto;

import kr.hhplus.be.server.domain.queue.QueueStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class QueueResponse {
    private Long queueId;
    private Long userId;
    private QueueStatus queueStatus;
    private LocalDateTime tokenCreatedAt;
    private LocalDateTime tokenExpiresAt;
}
