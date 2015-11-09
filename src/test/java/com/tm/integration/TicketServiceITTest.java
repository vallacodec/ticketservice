package com.tm.integration;

import com.tm.config.DataBaseConfig;
import com.tm.config.RepositoryConfiguration;
import com.tm.repository.TicketServiceRepository;
import com.tm.service.impl.TicketServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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


    public TicketServiceITTest(){
    }

    @Before
    public void setUp() throws Exception {
        ticketService = new TicketServiceImpl(ticketServiceRepository);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSeatCountInEachLevel() {
        Assert.assertEquals("Seats in Balcony 1", 25, ticketService.numSeatsAvailable(Optional.of(1)));
        Assert.assertEquals("Seats in Balcony 1", 20, ticketService.numSeatsAvailable(Optional.of(2)));
        Assert.assertEquals("Seats in Balcony 1", 15, ticketService.numSeatsAvailable(Optional.of(3)));
        Assert.assertEquals("Seats in Balcony 1", 15, ticketService.numSeatsAvailable(Optional.of(4)));
    }

}
