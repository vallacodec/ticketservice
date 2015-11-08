package com.tm.model;


import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by svallaban1 on 11/3/2015.
 */
@Data
public class SeatHold {

    public static long SEAT_HOLD_DURATION = 5 * 60 * 1000;

    private Integer seatHoldId;

    private List<Seat> seats;

    private Date seatHoldTime;

    private String customerEmailId;

    /**
     * This method holds the seat
     */
    public Seat holdSeat(com.tm.persistence.Seat seatPersistence) {
        Seat seat = new Seat();
        seat.setSeatNo(seatPersistence.getSeatNo());
        seat.setSeatHoldId(seatPersistence.getSeatHoldId());
        seat.setStatus(SeatStatus.HOLD);
        seat.setSeatHoldTime(new Date());
        return seat;
    }

    public void unHoldSeat(Seat seat) {
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setSeatHoldTime(null);
    }

}
