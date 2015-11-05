package com.tm.service.impl;

import com.tm.model.Level;
import com.tm.model.Seat;
import com.tm.model.SeatHold;
import com.tm.model.Status;
import com.tm.model.Venue;
import com.tm.service.TicketService;

import java.util.List;
import java.util.Optional;

/**
 * Created by svallaban1 on 11/3/2015.
 */
public class TicketServiceImpl implements TicketService {
    /**
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return
     */
    public int numSeatsAvailable(Optional<Integer> venueLevel) {


        return 0;
    }

    /**
     * @param numSeats      the number of seats to find and hold
     * @param minLevel      the minimum venue level
     * @param maxLevel      the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return
     */
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {

        Venue venue = new Venue();
        List<Level> levels = venue.getLevels();
        for (Level level : levels) {
            for (Seat seat : level.getSeats()) {
                seat.setStatus(Status.HOLD);
            }


        }
        return null;
    }

    /**
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
     *                      is assigned
     * @return
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        return null;
    }
}
