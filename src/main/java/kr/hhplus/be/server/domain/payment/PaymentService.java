package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.common.exception.AlreadyExistsException;
import kr.hhplus.be.server.domain.concert.Schedule;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.concert.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createPayment(Schedule schedule, Seat seat, Reservation reservation) {

        Payment payment = paymentRepository.findByReservationId(reservation.getId());
        Payment savedPayment = null;
        if(payment == null){
            Payment createdPayment = new Payment().create(schedule, seat, reservation);
            savedPayment = paymentRepository.save(createdPayment);
        } else {
            throw new AlreadyExistsException("이미 결제되었습니다.");
        }
        return savedPayment;
    }
}
