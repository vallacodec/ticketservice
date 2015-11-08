package com.tm.persistence;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by indsara on 11/7/15.
 */
@Entity
@Data
public class SeatHold {

    @Id
    private Integer seatHoldId;

    private Date seatHoldTime;

    private String customerEmailId;

}
