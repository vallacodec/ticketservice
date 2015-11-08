package com.tm.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by svallaban1 on 11/3/2015.
 * This class holds the information about the seats in the venue
 */
@Data
public class Seat {

    private Integer seatNo;

    private SeatStatus status;

    private Level level;

    private String emailId;

    private Integer seatHoldId;

    private Date seatHoldTime;

}
