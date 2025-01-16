package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.LackRemainingTicketExcption;
import kr.hhplus.be.server.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "schedule")
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime concertDateTime;

    @Column(nullable = false)
    private LocalDateTime bookingStart;

    @Column(nullable = false)
    private LocalDateTime bookingEnd;

    private int remainingTicket;

    private int totalTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Concert concert;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Seat> seats;

    @Builder
    public Schedule(Long scheduleId, BigDecimal price, LocalDateTime concertDateTime,
                    LocalDateTime bookingStart, LocalDateTime bookingEnd, int remainingTicket, int totalTicket,
                    Concert concert, List<Seat> seats){
        this.id = scheduleId;
        this.price = price;
        this.concertDateTime = concertDateTime;
        this.bookingStart = bookingStart;
        this.bookingEnd = bookingEnd;
        this.remainingTicket = remainingTicket;
        this.totalTicket = totalTicket;
        this.concert = concert;
        this.seats = seats;
    }


    public void addSeat(Seat seat) {
        if(this.seats == null){
            this.seats = new ArrayList<>();
        }

        Seat updatedSeat = Seat.builder()
                .seatNumber(seat.getSeatNumber())
                .seatStatus(seat.getSeatStatus())
                .seatPrice(seat.getSeatPrice())
                .schedule(this)
                .build();

        this.seats.add(updatedSeat);
    }

    public Schedule update(Schedule schedule, int increaseOrDecreaseNumber) {
        if(schedule.getRemainingTicket() < 1){
            throw new LackRemainingTicketExcption("잔여 좌석이 없습니다.");
        }

        int changeRemainingTicketNumber = schedule.getRemainingTicket() + increaseOrDecreaseNumber;

        return Schedule.builder()
                .scheduleId(schedule.getId())
                .price(schedule.getPrice())
                .concertDateTime(schedule.getConcertDateTime())
                .bookingStart(schedule.getBookingStart())
                .bookingEnd(schedule.getBookingEnd())
                .remainingTicket(changeRemainingTicketNumber)
                .totalTicket(schedule.getTotalTicket())
                .build();
    }
}
