package com.tm.service.impl;

import com.tm.model.LevelEnum;
import com.tm.model.SeatStatus;
import com.tm.persistence.Seat;
import com.tm.repository.TicketServiceRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TicketServiceImplTest {


    @Mock
    private TicketServiceRepository ticketServiceRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Before
    public void setUp() throws Exception {
        ticketService = new TicketServiceImpl(ticketServiceRepository);
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testNumSeatsAvailableIfLevelNotPresent() throws Exception {
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat());
        seats.add(new Seat());
        when(ticketServiceRepository.getSeatDetails(null)).thenReturn(seats);
        int seatCount = ticketService.numSeatsAvailable(null);
        Assert.assertEquals("Seat count returned not matched", seatCount, 2);

    }

    @Test
    public void testNumSeatsAvailableIfLevelPresent() throws Exception {
        List<Seat> seatsOrchestra = new ArrayList<>();
        List<Seat> seatsBalcony = new ArrayList<>();
        Seat seatOrchestra = new Seat();
        seatOrchestra.setSeatNo(1);
        seatOrchestra.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra.setLevelId(LevelEnum.ORCHESTRA.getLevel());

        Seat seatOrchestra1 = new Seat();
        seatOrchestra1.setSeatNo(2);
        seatOrchestra1.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra1.setLevelId(LevelEnum.ORCHESTRA.getLevel());
        seatsOrchestra.add(seatOrchestra1);
        seatsOrchestra.add(seatOrchestra);

        Seat seatBalcony = new Seat();
        seatOrchestra.setSeatNo(3);
        seatOrchestra.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra.setLevelId(LevelEnum.BALCONY1.getLevel());
        seatsBalcony.add(seatBalcony);

        when(ticketServiceRepository.getSeatDetails(Optional.of(LevelEnum.ORCHESTRA.getLevel()))).thenReturn(seatsOrchestra);
        when(ticketServiceRepository.getSeatDetails(Optional.of(LevelEnum.BALCONY1.getLevel()))).thenReturn(seatsBalcony);

        int seatCount = ticketService.numSeatsAvailable(Optional.of(LevelEnum.ORCHESTRA.getLevel()));
        Assert.assertEquals("Seat count returned not matched", seatCount, 2);

        seatCount = ticketService.numSeatsAvailable(Optional.of(LevelEnum.BALCONY1.getLevel()));
        Assert.assertEquals("Seat count returned not matched", seatCount, 1);

    }

    @Test
    public void testFindAndHoldSeatsWithSeatCountOnly() throws Exception {

        List<Seat> seatsOrchestra = new ArrayList<>();
        List<Seat> seatsBalcony = new ArrayList<>();
        Seat seatOrchestra = new Seat();
        seatOrchestra.setSeatNo(1);
        seatOrchestra.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra.setLevelId(LevelEnum.ORCHESTRA.getLevel());

        Seat seatOrchestra1 = new Seat();
        seatOrchestra1.setSeatNo(2);
        seatOrchestra1.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra1.setLevelId(LevelEnum.ORCHESTRA.getLevel());

        Seat seatOrchestra2 = new Seat();
        seatOrchestra2.setSeatNo(3);
        seatOrchestra2.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra2.setLevelId(LevelEnum.ORCHESTRA.getLevel());
        seatsOrchestra.add(seatOrchestra1);
        seatsOrchestra.add(seatOrchestra);
        seatsOrchestra.add(seatOrchestra2);

        Seat seatBalcony = new Seat();
        seatOrchestra.setSeatNo(4);
        seatOrchestra.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra.setLevelId(LevelEnum.BALCONY1.getLevel());
        seatsBalcony.add(seatBalcony);

        when(ticketServiceRepository.getSeatDetails(any())).thenReturn(seatsBalcony, seatsBalcony, seatsBalcony, seatsOrchestra);
        com.tm.model.SeatHold seatHold = ticketService.findAndHoldSeats(3, Optional.empty(), Optional.empty(), "test@gmail.com");
        Assert.assertEquals("seathold didn't happen properly", 3, seatHold.getSeats().size());


    }

    @Test
    public void testFindAndHoldSeatsWithSeatCountAndMinLevelOnly() throws Exception {

        List<Seat> seatsOrchestra = new ArrayList<>();
        List<Seat> seatsBalcony = new ArrayList<>();
        Seat seatOrchestra = new Seat();
        seatOrchestra.setSeatNo(1);
        seatOrchestra.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra.setLevelId(LevelEnum.ORCHESTRA.getLevel());

        Seat seatOrchestra1 = new Seat();
        seatOrchestra1.setSeatNo(2);
        seatOrchestra1.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra1.setLevelId(LevelEnum.ORCHESTRA.getLevel());

        Seat seatOrchestra2 = new Seat();
        seatOrchestra2.setSeatNo(3);
        seatOrchestra2.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra2.setLevelId(LevelEnum.ORCHESTRA.getLevel());
        seatsOrchestra.add(seatOrchestra1);
        seatsOrchestra.add(seatOrchestra);
        seatsOrchestra.add(seatOrchestra2);

        Seat seatBalcony = new Seat();
        seatOrchestra.setSeatNo(4);
        seatOrchestra.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra.setLevelId(LevelEnum.BALCONY1.getLevel());
        seatsBalcony.add(seatBalcony);

        when(ticketServiceRepository.getSeatDetails(any())).thenReturn(seatsBalcony, seatsBalcony, seatsOrchestra);
        com.tm.model.SeatHold seatHold = ticketService.findAndHoldSeats(3, Optional.of(2), Optional.empty(), "test@gmail.com");
        verify(ticketServiceRepository, times(4)).getSeatDetails(any());
        Assert.assertEquals("seathold didn't happen properly", 3, seatHold.getSeats().size());


    }

    @Test
    public void testFindAndHoldSeatsWithSeatCountAndMinMaxLevels() throws Exception {

        List<Seat> seatsOrchestra = new ArrayList<>();
        List<Seat> seatsBalcony = new ArrayList<>();
        Seat seatOrchestra = new Seat();
        seatOrchestra.setSeatNo(1);
        seatOrchestra.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra.setLevelId(LevelEnum.ORCHESTRA.getLevel());

        Seat seatOrchestra1 = new Seat();
        seatOrchestra1.setSeatNo(2);
        seatOrchestra1.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra1.setLevelId(LevelEnum.ORCHESTRA.getLevel());

        Seat seatOrchestra2 = new Seat();
        seatOrchestra2.setSeatNo(3);
        seatOrchestra2.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra2.setLevelId(LevelEnum.ORCHESTRA.getLevel());
        seatsOrchestra.add(seatOrchestra1);
        seatsOrchestra.add(seatOrchestra);
        seatsOrchestra.add(seatOrchestra2);

        Seat seatBalcony = new Seat();
        seatOrchestra.setSeatNo(4);
        seatOrchestra.setSeatStatus(SeatStatus.AVAILABLE.getStatusId());
        seatOrchestra.setLevelId(LevelEnum.BALCONY1.getLevel());
        seatsBalcony.add(seatBalcony);

        when(ticketServiceRepository.getSeatDetails(any())).thenReturn(seatsBalcony, seatsBalcony, seatsOrchestra);
        com.tm.model.SeatHold seatHold = ticketService.findAndHoldSeats(3, Optional.of(2), Optional.of(4), "test@gmail.com");
        verify(ticketServiceRepository, times(4)).getSeatDetails(any());
        Assert.assertEquals("seathold didn't happen properly", 3, seatHold.getSeats().size());


    }

    @Test
    public void testReserveSeatsIfNoHoldRecord() throws Exception {
        when(ticketServiceRepository.getSeatHoldTime(1)).thenReturn(null);
        Assert.assertEquals("Record not found test case failed", TicketServiceImpl.NO_RECORD_FOUND, ticketService.reserveSeats(1, "test@gmail.com"));
    }


    @Test
    public void testReserveSeatsIfHoldTimeLessThanMaxHoldTime() throws Exception {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -1);
        when(ticketServiceRepository.getSeatHoldTime(1)).thenReturn(now.getTime());
        Assert.assertEquals("Record not found test case failed", TicketServiceImpl.SUCCESS, ticketService.reserveSeats(1, "test@gmail.com"));
    }

    @Test
    public void testReserveSeatsIfHoldTimeGreaterThanMaxHoldTime() throws Exception {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -3);
        when(ticketServiceRepository.getSeatHoldTime(1)).thenReturn(now.getTime());
        Assert.assertEquals("Record not found test case failed", TicketServiceImpl.HOLD_TIME_OUT, ticketService.reserveSeats(1, "test@gmail.com"));
    }

    @After
    public void tearDown() throws Exception {
        ticketService = null;

    }

}
