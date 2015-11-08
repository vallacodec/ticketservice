package com.tm.service.impl;

import com.tm.persistence.Seat;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TicketServiceImplTest {


    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query query;

    @InjectMocks
    private TicketServiceImpl ticketService;


    @Before
    public void setUp() throws Exception {
        ticketService = new TicketServiceImpl();
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testNumSeatsAvailable() throws Exception {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.createQuery(anyString())).thenReturn(query);
        List<Seat> seats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setSeatNo(1);
        seats.add(seat);
        when(query.list()).thenReturn(seats);
        int seatCount = ticketService.numSeatsAvailable(Optional.of(1));
        Assert.assertEquals(seatCount, 1);
    }

    @Test
    public void testFindAndHoldSeats() throws Exception {

    }

    @Test
    public void testReserveSeats() throws Exception {

    }
}
