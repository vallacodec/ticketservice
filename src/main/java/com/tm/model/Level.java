package com.tm.model;

import java.util.List;

/**
 * Created by svallaban1 on 11/3/2015.
 */
public class Level {

    private List<Seat> seats;

    private LevelEnum levelEnum;

    private int availableSeats;

    private String prize;

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public LevelEnum getLevelEnum() {
        return levelEnum;
    }

    public void setLevelEnum(LevelEnum levelEnum) {
        this.levelEnum = levelEnum;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int avialableSeats) {
        this.availableSeats = avialableSeats;
    }
}
