package com.tm.service.impl;

import com.tm.model.LevelEnum;
import com.tm.model.SeatHold;
import com.tm.model.SeatStatus;
import com.tm.persistence.Seat;
import com.tm.repository.TicketServiceRepository;
import com.tm.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);


    private TicketServiceRepository ticketServiceRepository;

    //Seat hold time is 2 minutes
    private static final int HOLD_TIME = 2 * 60 * 1000;

    public static final String SUCCESS = "SUCCESS";

    public static final String HOLD_TIME_OUT = "hold time out";

    public static final String NO_RECORD_FOUND = "No Record Found";

    @Autowired
    public TicketServiceImpl(@Qualifier("ticketServiceRepository")TicketServiceRepository ticketServiceRepository){
        this.ticketServiceRepository = ticketServiceRepository;
    }
    /**
     * venueLevel 4-Orchestra 3-Main 2-Balcony1 1-Balcony2
     *
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return
     */
    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        List<Seat> seats = ticketServiceRepository.getSeatDetails(venueLevel);
        return seats.size();
    }

    /**
     * This method holds the seats for the customerEmail and no of seats
     * if seats are not available in the minLevel then the method looks in the other level to
     * find the seat until maxLevel (please check seatAvailableLevel javadoc for more detail)
     * This method assumes the user wants all the seats to be in one level
     *
     * @param numSeats      the number of seats to find and hold
     * @param minLevel      the minimum venue level
     * @param maxLevel      the maximum venue level
     * @param customerEmail unique identifier for the customer
     * @return
     */
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
        SeatHold seatHold = null;
        if (numSeats < 0 || customerEmail == null) {
            log.error("number of seats and customer email id are mandatory to hold the seats");
            return null;
        }
        Integer levelInteger = seatAvailableLevel(numSeats, minLevel, maxLevel);
        if (levelInteger > 0) {
            List<Seat> seats = ticketServiceRepository.getSeatDetails(Optional.of(levelInteger));
            List<com.tm.model.Seat> seatHolds = new ArrayList<>();
            //hold seats
            seatHold = new SeatHold();
            seatHold.setSeatHoldTime(new Date());
            int seatHoldId = ticketServiceRepository.insertSeatHoldData(seatHold);
            for (int i = 0; i < numSeats; i++) {
                com.tm.model.Seat seat = seatHold.holdSeat(seats.get(i));
                ticketServiceRepository.updateSeatDetailsForHold(seat, seatHoldId);
                seatHolds.add(seat);
            }
            seatHold.setSeatHoldId(seatHoldId);
            seatHold.setSeats(seatHolds);
        }
        return seatHold;
    }

    /**
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
     *                      is assigned
     * @return
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        String reserved = NO_RECORD_FOUND;
        Date date = ticketServiceRepository.getSeatHoldTime(seatHoldId);
        Date now = new Date();
        if (date != null) {
            if (now.getTime() - date.getTime() > HOLD_TIME) {
                ticketServiceRepository.deleteSeatHold(seatHoldId);
                ticketServiceRepository.updateSeatForAvailableOrReserved(seatHoldId, SeatStatus.AVAILABLE);
                reserved = HOLD_TIME_OUT;
            } else {
                ticketServiceRepository.updateSeatForAvailableOrReserved(seatHoldId, SeatStatus.RESERVED);
                ticketServiceRepository.deleteSeatHold(seatHoldId);
                reserved = SUCCESS;
            }
        }
        return reserved;
    }

    /**
     * This method always return the lowest possible level unless the min level or max level are present
     * If minimum level is not present and max level is present, the the method finds the seats
     * with the lowest level which is balcony1
     * if maximum level is not present then this method starts the seats search from the minLevel
     * until the orchestra
     * This method returns the first available level for the no of seats
     *
     * @param numSeats
     * @param minLevel
     * @param maxLevel
     * @return
     */
    private int seatAvailableLevel(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel) {
        int availableLevel = -1;
        if (minLevel.isPresent() && maxLevel.isPresent()) {
            availableLevel = findLevelAvailable(numSeats, minLevel.get(), maxLevel.get());
        } else if (minLevel.isPresent()) {
            availableLevel = findLevelAvailable(numSeats, minLevel.get(), LevelEnum.ORCHESTRA.getLevel());
        } else if (maxLevel.isPresent()) {
            availableLevel = findLevelAvailable(numSeats, LevelEnum.BALCONY2.getLevel(), maxLevel.get());
        } else {
            availableLevel = findLevelAvailable(numSeats, LevelEnum.BALCONY2.getLevel(), LevelEnum.ORCHESTRA.getLevel());
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
        int availableLevel = minValue - 1;
        for (int i = minValue; i <= maxValue; i++) {
            availableLevel = availableLevel + 1;
            if (numSeatsAvailable(Optional.of(i)) >= numSeats) {
                break;
            }
        }
        return availableLevel;
    }
}
