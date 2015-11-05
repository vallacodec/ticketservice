package com.tm.model;

/**
 * Created by svallaban1 on 11/3/2015.
 */
public class SeatHold extends Seat {

    public boolean hold;

    /**
     * This method holds the seat
     */
    public void holdSeat() {
        String seatNo = this.getSeatNo();
        this.setStatus(Status.HOLD);
        this.setHold(true);
        //call the db or service to hold the seat
    }

    public boolean isHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }
}
