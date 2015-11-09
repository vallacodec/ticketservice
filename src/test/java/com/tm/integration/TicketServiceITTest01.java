package com.tm.integration;

import com.tm.model.SeatHold;
import com.tm.repository.TicketServiceRepository;
import com.tm.service.impl.TicketServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Created by svallaban1 on 11/9/2015.
 */
public class TicketServiceITTest01 extends TicketServiceBaseITTest {

    @Autowired
    private TicketServiceRepository ticketServiceRepository;

    private TicketServiceImpl ticketService;


    public TicketServiceITTest01() {
    }

    @Before
    public void setUp() throws Exception {
        ticketService = new TicketServiceImpl(ticketServiceRepository);
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * #Test Case 02
     */
    @Test
    public void testHoldSeatWhenLevelIsNotSend() {

        Assert.assertEquals("Total # of seats when level id is not passed", 75, ticketService.numSeatsAvailable(Optional.<Integer>empty()));
        Assert.assertEquals("Seats in Balcony 2", 25, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 20, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in main", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Orchestra", 15, ticketService.numSeatsAvailable(Optional.of(4)));

        SeatHold seatHold = ticketService.findAndHoldSeats(3, Optional.empty(), Optional.empty(), "test@gmail.com");
        Assert.assertEquals("seat No", 1, seatHold.getSeats().get(0).getSeatNo().intValue());

        Assert.assertEquals("Total # of seats when level id is not passed", 22, ticketService.numSeatsAvailable(Optional.of(1)));
        SeatHold seatHold1 = ticketService.findAndHoldSeats(20, Optional.empty(), Optional.empty(), "test@gmail.com");
        Assert.assertEquals("seat No", 4, seatHold1.getSeats().get(0).getSeatNo().intValue());
        Assert.assertEquals("seats reserved", 20, seatHold1.getSeats().size());


        Assert.assertEquals("Seats in Balcony 2", 2, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 20, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in main", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Orchestra", 15, ticketService.numSeatsAvailable(Optional.of(4)));

        Assert.assertEquals("The reservation is successful ", TicketServiceImpl.SUCCESS, ticketService.reserveSeats(seatHold.getSeatHoldId(), "test@gmail.com"));
    }
}
