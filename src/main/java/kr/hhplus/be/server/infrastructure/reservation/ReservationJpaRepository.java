package kr.hhplus.be.server.infrastructure.reservation;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.id = :reservationId AND r.seatId = :seatId")
    Optional<Reservation> findByReservationIdAndSeatIdWithLock(@Param("reservationId") Long reservationId, @Param("seatId") Long seatId);

    @Query("SELECT r FROM Reservation r WHERE r.expiredAt < :now")
    List<Reservation> findExpiredReservation(@Param("now") LocalDateTime now);


    @Query("SELECT r FROM Reservation r WHERE r.seatId = :seatId")
    Reservation findBySeatId(@Param("seatId") Long seatId);
}
