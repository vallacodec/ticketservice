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
public class TicketServiceITTest02 extends TicketServiceBaseITTest {

    @Autowired
    private TicketServiceRepository ticketServiceRepository;

    private TicketServiceImpl ticketService;


    public TicketServiceITTest02() {
    }

    @Before
    public void setUp() throws Exception {
        ticketService = new TicketServiceImpl(ticketServiceRepository);
    }

    /**
     * #Test Case 03
     */
    @Test
    public void testHoldSeatBetweenTwoLevelWhenOneLevelGetsHoldedFull() {

        Assert.assertEquals("Total # of seats when level id is not passed", 75, ticketService.numSeatsAvailable(Optional.<Integer>empty()));
        Assert.assertEquals("Seats in Balcony 2", 25, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 20, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in main", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Orchestra", 15, ticketService.numSeatsAvailable(Optional.of(4)));

        SeatHold seatHold = ticketService.findAndHoldSeats(3, Optional.of(1), Optional.of(3), "test@gmail.com");
        Assert.assertEquals("seat No", seatHold.getSeats().get(0).getSeatNo().intValue(), 1);
        Assert.assertEquals("Total # of seats when level id is not passed", 72, ticketService.numSeatsAvailable(Optional.<Integer>empty()));
        Assert.assertEquals("Seats in Balcony 2", 22, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 20, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in main", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Orchestra", 15, ticketService.numSeatsAvailable(Optional.of(4)));




        SeatHold seatHold1 = ticketService.findAndHoldSeats(21, Optional.of(1), Optional.of(3), "test@gmail.com");
        Assert.assertEquals("seat No", seatHold1.getSeats().get(0).getSeatNo().intValue(), 4);
        Assert.assertEquals("Total # of seats when level id is not passed", 51, ticketService.numSeatsAvailable(Optional.<Integer>empty()));
        Assert.assertEquals("Seats in Balcony 2", 1, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 20, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in main", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Orchestra", 15, ticketService.numSeatsAvailable(Optional.of(4)));


        SeatHold seatHold2 = ticketService.findAndHoldSeats(3, Optional.of(1), Optional.of(3), "test@gmail.com");
        Assert.assertEquals("seat No", seatHold2.getSeats().get(0).getSeatNo().intValue(), 26);
        Assert.assertEquals("Total # of seats when level id is not passed", 48, ticketService.numSeatsAvailable(Optional.<Integer>empty()));
        Assert.assertEquals("Seats in Balcony 2", 1, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 17, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in main", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Orchestra", 15, ticketService.numSeatsAvailable(Optional.of(4)));

        Assert.assertEquals("The reservation is successful ", TicketServiceImpl.SUCCESS, ticketService.reserveSeats(seatHold.getSeatHoldId(), "test@gmail.com"));
    }

    @After
    public void tearDown() throws Exception {

    }
}
