package kr.hhplus.be.server.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation createdReservation);

    Optional<Reservation> findByReservationIdAndSeatIdWithLock(Long reservationId, Long seatId);

    List<Reservation> findExpiredReservation(LocalDateTime now);

    Reservation findBySeatId(Long seatId);
}
