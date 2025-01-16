package kr.hhplus.be.server.infrastructure.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    @Override
    public Reservation save(Reservation createdReservation) {
        return reservationJpaRepository.save(createdReservation);
    }

    @Override
    public Optional<Reservation> findByReservationIdAndSeatIdWithLock(Long reservationId, Long seatId) {
        return reservationJpaRepository.findByReservationIdAndSeatIdWithLock(reservationId, seatId);
    }

    @Override
    public List<Reservation> findExpiredReservation(LocalDateTime now) {
        return reservationJpaRepository.findExpiredReservation(now);
    }

    @Override
    public Reservation findBySeatId(Long seatId) {
        return reservationJpaRepository.findBySeatId(seatId);
    }
}
