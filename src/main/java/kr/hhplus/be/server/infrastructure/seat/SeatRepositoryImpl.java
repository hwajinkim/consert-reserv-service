package kr.hhplus.be.server.infrastructure.seat;

import kr.hhplus.be.server.domain.seat.Seat;
import kr.hhplus.be.server.domain.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;
    @Override
    public Optional<Seat> findById(Long seatId) {
        return seatJpaRepository.findById(seatId);
    }

    @Override
    public Seat save(Seat updatedSeat) {
        return seatJpaRepository.save(updatedSeat);
    }

    @Override
    public Optional<Object> findScheduleIdBySeatId(Long seatId) {
        return seatJpaRepository.findScheduleIdBySeatId(seatId);
    }

    @Override
    public Optional<Seat> findByIdWithLock(Long seatId) {
        return seatJpaRepository.findByIdWithLock(seatId);
    }
}
