package com.tm.service.impl;

import com.tm.model.Level;
import com.tm.model.LevelEnum;
import com.tm.model.SeatHold;
import com.tm.model.SeatStatus;
import com.tm.persistence.Seat;
import com.tm.repository.LevelRepository;
import com.tm.repository.TicketRepository;
import com.tm.repository.TicketServiceRepository;
import com.tm.service.TicketService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by svallaban1 on 11/3/2015.
 */
@Service
@EnableTransactionManagement
public class TicketServiceImpl implements TicketService {


    private TicketServiceRepository ticketServiceRepository;

    private static final int HOLD_TIME = 2 * 60 * 1000;

    private static final String SUCCESS = "SUCCESS";

    private static final String HOLD_TIME_OUT = "hold time out";


    /**
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return
     */
    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        List<Seat> seats = ticketServiceRepository.getSeatDetails(venueLevel);
        return seats.size();
    }

    /**
     * This method holds the seats for the customerEmail if available
     * if seats are not available in the minLevel then the method looks in the other level to
     * find the seat until maxLevel
     * This method assumes the user wants all the seats to be in one level
     *
     * @param numSeats      the number of seats to find and hold
     * @param minLevel      the minimum venue level
     * @param maxLevel      the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return
     */
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
        Integer levelInteger = seatAvailableLevel(numSeats, minLevel, maxLevel);
        List<Seat> seats = ticketServiceRepository.getSeatDetails(Optional.of(levelInteger));
        List<com.tm.model.Seat> seatHolds = new ArrayList<>();
        //hold seats
        SeatHold seatHold = new SeatHold();
        seatHold.setSeatHoldTime(new Date());
        int seatHoldId = ticketServiceRepository.insertSeatHoldData(seatHold);
        for (int i = 0; i < numSeats; i++) {
            com.tm.model.Seat seat = seatHold.holdSeat(seats.get(i));
            ticketServiceRepository.updateSeatDetailsForHold(seat, seatHoldId);
            seatHolds.add(seat);
        }
        seatHold.setSeatHoldId(seatHoldId);
        seatHold.setSeats(seatHolds);
        return seatHold;
    }

    /**
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
     *                      is assigned
     * @return
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        String reserved = "";
        Date date = ticketServiceRepository.getSeatHoldTime(seatHoldId);
        if (date.getTime() > HOLD_TIME) {
            ticketServiceRepository.deleteSeatHold(seatHoldId);
            ticketServiceRepository.updateSeatForAvailableOrReserved(seatHoldId, SeatStatus.AVAILABLE);
            reserved = HOLD_TIME_OUT;
        } else {
            ticketServiceRepository.updateSeatForAvailableOrReserved(seatHoldId, SeatStatus.RESERVED);
            ticketServiceRepository.deleteSeatHold(seatHoldId);
            reserved = SUCCESS;
        }
        return reserved;
    }

    /**
     * If minimum level is not present, it assumes to be 1
     * if maximum level is not present then this method assumes the minLevel is maxLevel
     * This method returns the first available level for the no of seats
     *
     * @param numSeats
     * @param minLevel
     * @param maxLevel
     * @return
     */
    private int seatAvailableLevel(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel) {
        int availableLevel = 0;
        if (minLevel.isPresent() && maxLevel.isPresent()) {
            availableLevel = findLevelAvailable(numSeats, minLevel.get(), maxLevel.get());
        } else if (minLevel.isPresent()) {
            availableLevel = findLevelAvailable(numSeats, 1, minLevel.get());
        } else if (maxLevel.isPresent()) {
            availableLevel = findLevelAvailable(numSeats, 1, maxLevel.get());
        }
        return availableLevel;
    }

    /**
     * This method find the available level based on the seat count, min and max level input
     *
     * @param numSeats
     * @param minValue
     * @param maxValue
     * @return
     */
    private int findLevelAvailable(int numSeats, int minValue, int maxValue) {
        int availableLevel = 0;
        for (int i = minValue; i < maxValue; i++) {
            if (numSeats >= numSeatsAvailable(Optional.of(i))) {
                availableLevel = availableLevel + 1;
                break;
            }
        }
        return availableLevel;
    }
}
