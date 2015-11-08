package com.tm.service.impl;

import com.tm.model.Level;
import com.tm.model.LevelEnum;
import com.tm.model.SeatHold;
import com.tm.model.SeatStatus;
import com.tm.persistence.Seat;
import com.tm.repository.LevelRepository;
import com.tm.repository.TicketRepository;
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

    private TicketRepository ticketRepository;

    private LevelRepository levelRepository;

    @Autowired
    private SessionFactory sessionFactory;

    private static String QUERY_WITH_LEVEL_ID = "from Seat where levelId = :levelId and status = 2  ";

    private static String QUERY_WITHOUT_LEVEL_ID = "from Seat where status = 2";

    private static String FIND_AVAILABLE_SEATS = "from Seat where levelId= :levelId";


    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Autowired
    public void setLevelRepository(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    /**
     * @param venueLevel a numeric venue level identifier to limit the search
     * @return
     */
    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        Session session = sessionFactory.openSession();
        Query query = null;
        if (venueLevel.isPresent()) {
            query = session.createQuery(QUERY_WITH_LEVEL_ID);
        } else {
            query = session.createQuery(QUERY_WITHOUT_LEVEL_ID);
        }
        query.setParameter("levelId", venueLevel);
        List<Seat> results = query.list();
        return results.size();
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
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(QUERY_WITH_LEVEL_ID);
        query.setParameter("levelId", levelInteger);
        List<Seat> results = query.list();
        //hold seats

        SeatHold seatHold = new SeatHold();
        List<com.tm.model.Seat> seats = new ArrayList<>();
        seatHold.holdSeat(seats);
        //insert into seatHold table
        //update in the Seat table
        seatHold.setSeats(seats);

        return seatHold;
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


    private int seatAvailableLevel(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel) {
        Integer availableLevel = null;
        for (int i = 1; i < maxLevel.get(); i++) {
            if (numSeats >= numSeatsAvailable(Optional.of(i))) {
                availableLevel = i;
            }
        }
        return availableLevel;
    }
}
