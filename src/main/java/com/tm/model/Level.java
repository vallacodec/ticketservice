package com.tm.model;

import lombok.Data;

/**
 * Created by svallaban1 on 11/3/2015.
 * This model holds the level details which includes the levelName, total no of seats in that level
 * and the cost of that level
 */
@Data
public class Level {

    private LevelEnum levelEnum;

    private int totalNoOfSeats;

    private String cost;

    private int availableSeats;

}
