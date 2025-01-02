package kr.hhplus.be.server.interfaces.api.dto;

import kr.hhplus.be.server.domain.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private Long reservationId;
    private Long seatId;
    private String concertName;
    private LocalDateTime concertDateTime;
    private double amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTime;
}
