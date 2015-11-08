package com.tm.repository;

import com.tm.config.DataBaseConfig;
import com.tm.config.RepositoryConfiguration;
import com.tm.model.Seat;
import com.tm.model.SeatHold;
import com.tm.model.SeatStatus;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by svallaban1 on 11/6/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class,
        DataBaseConfig.class})
public class TicketServiceRepositoryTest {


    private static final Logger log = LoggerFactory.getLogger(TicketServiceRepositoryTest.class);

    private TicketServiceRepository ticketServiceRepository;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DataSource dataSource;


    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
        ticketServiceRepository = new TicketServiceRepository(sessionFactory);
    }

    @After
    public void tearDown() throws Exception {
        ticketServiceRepository = null;
    }

    @Test
    public void testInsertSeatHoldData() throws Exception {
        SeatHold seatHold = new SeatHold();
        Date seatHoldTime = new Date();
        List<Seat> seats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setSeatNo(1);
        seat.setSeatHoldTime(seatHoldTime);
        seatHold.setSeats(seats);
        seatHold.setSeatHoldTime(seatHoldTime);
        seatHold.setCustomerEmailId("test@gmail.com");

        int seatHoldId = ticketServiceRepository.insertSeatHoldData(seatHold);
        Assert.assertNotNull("seatHoldId is not null", seatHoldId);
    }

    @Test
    public void testUpdateSeatDetailsForHold() throws Exception {
        String sql = "SELECT seat_no,seat_hold_id FROM seat WHERE seat_hold_id = ?";
        Seat seat = new Seat();
        seat.setSeatNo(1);
        ticketServiceRepository.updateSeatDetailsForHold(seat, 1);
        jdbcTemplate.query(sql, new Object[]{1}, (rs, rowNum) -> rs.getInt("Seat_no")
        ).forEach(seatNo -> Assert.assertEquals("1", seatNo.toString()));

    }

    @Test
    public void testUpdateSeatForAvailable() throws Exception {
        Seat seat = new Seat();
        seat.setSeatNo(1);
        ticketServiceRepository.updateSeatDetailsForHold(seat, 1);
        int updated = ticketServiceRepository.updateSeatForAvailableOrReserved(1, SeatStatus.AVAILABLE);
        Assert.assertEquals("Updated seat count",1,updated);


    }

    @Test
    public void testDeleteSeatHold() throws Exception {
        SeatHold seatHold = new SeatHold();
        Date seatHoldTime = new Date();
        List<Seat> seats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setSeatNo(1);
        seat.setSeatHoldTime(seatHoldTime);
        seatHold.setSeats(seats);
        seatHold.setSeatHoldTime(seatHoldTime);
        seatHold.setCustomerEmailId("test@gmail.com");

        int seatHoldId = ticketServiceRepository.insertSeatHoldData(seatHold);

        int count = ticketServiceRepository.deleteSeatHold(seatHoldId);
        Assert.assertEquals("deleted count ",count,1);

    }

    @Test
    public void testGetSeatDetails() throws Exception {
        ticketServiceRepository.getSeatDetails(Optional.of(1));
    }
}
