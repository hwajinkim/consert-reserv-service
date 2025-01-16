package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.common.exception.AlreadyExistsException;
import kr.hhplus.be.server.common.exception.ReservationBadStatusException;
import kr.hhplus.be.server.common.exception.ReservationNotFoundException;
import kr.hhplus.be.server.domain.concert.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Reservation creatSeatReservation(Seat seat, Long userId) {
        Reservation reservation = reservationRepository.findBySeatId(seat.getId());
        Reservation savedReservation = null;
        if(reservation == null){
            Reservation createdReservation =  new Reservation().create(seat, userId);
            savedReservation =  reservationRepository.save(createdReservation);
        } else {
            throw new AlreadyExistsException("이미 등록된 예약입니다.");
        }
        return savedReservation;
    }

    public Reservation updateReservation(Long reservationId, Long seatId) {
         Reservation reservation = reservationRepository.findByReservationIdAndSeatIdWithLock(reservationId, seatId)
                .orElseThrow(()-> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다."));

         if(reservation.getReservationState().equals(ReservationState.PANDING)){
             Reservation updateReservation = new Reservation().update(reservation.getId(), reservation.getSeatId(), reservation.getUserId(), ReservationState.PAID, reservation.getSeatPrice());
             reservationRepository.save(updateReservation);
         } else {
             throw new ReservationBadStatusException("유효하지 않은 예약 상태입니다.");
         }
         return reservation;
    }

    public List<Reservation> checkReservationExpiration() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservation(LocalDateTime.now());

        if(expiredReservations != null && !expiredReservations.isEmpty()){
            Reservation updatedReservation = null;
            for (Reservation reservation : expiredReservations) {
                updatedReservation = new Reservation().update(reservation.getId(), reservation.getSeatId(), reservation.getUserId(), ReservationState.CANCELLED, reservation.getSeatPrice());
            }
            reservationRepository.save(updatedReservation);
        }
        return expiredReservations;
    }
}
