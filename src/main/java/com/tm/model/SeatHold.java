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
    public void holdSeat(List<Seat> seats) {
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.HOLD);
            seat.setSeatHoldTime(new Date());
        }
    }

    public void unHoldSeat(Seat seat) {
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setSeatHoldTime(null);
    }

}
