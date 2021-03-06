package com.tm.integration;

import com.tm.config.DataBaseConfig;
import com.tm.config.RepositoryConfiguration;
import com.tm.model.SeatHold;
import com.tm.repository.TicketServiceRepository;
import com.tm.service.impl.TicketServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * Created by svallaban1 on 11/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class,
        DataBaseConfig.class})
public class TicketServiceITTest {

    @Autowired
    private TicketServiceRepository ticketServiceRepository;

    private TicketServiceImpl ticketService;


    public TicketServiceITTest() {
    }

    @Before
    public void setUp() throws Exception {
        ticketService = new TicketServiceImpl(ticketServiceRepository);
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * #Test Case 01
     */
    @Test
    public void testHoldSeatInLevelWhenLevelIsSend() {

        Assert.assertEquals("Total # of seats when level id is not passed", 75, ticketService.numSeatsAvailable(Optional.<Integer>empty()));
        Assert.assertEquals("Seats in Balcony 2", 25, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 20, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in main", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Orchestra", 15, ticketService.numSeatsAvailable(Optional.of(4)));

        SeatHold seatHold = ticketService.findAndHoldSeats(3, Optional.of(1), Optional.of(3), "test@gmail.com");
        Assert.assertEquals("seat No", seatHold.getSeats().get(0).getSeatNo().intValue(), 1);

        Assert.assertEquals("Total # of seats when level id is not passed", 22, ticketService.numSeatsAvailable(Optional.of(1)));
        SeatHold seatHold1 = ticketService.findAndHoldSeats(20, Optional.of(2), Optional.of(3), "test@gmail.com");
        Assert.assertEquals("seat No", seatHold1.getSeats().get(0).getSeatNo().intValue(), 26);

        Assert.assertEquals("Seats in Balcony 2", 22, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 0, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in main", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Orchestra", 15, ticketService.numSeatsAvailable(Optional.of(4)));


        Assert.assertEquals("The reservation is successful ", TicketServiceImpl.SUCCESS, ticketService.reserveSeats(seatHold.getSeatHoldId(), "test@gmail.com"));
    }

}
