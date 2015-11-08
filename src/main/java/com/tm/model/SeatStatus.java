package com.tm.model;

/**
 * Created by svallaban1 on 11/3/2015.
 */
public enum SeatStatus {
    HOLD(1),
    AVAILABLE(2),
    RESERVED(3);

    private Integer statusId;

    SeatStatus(int statusId) {
        this.statusId = statusId;

    }

    public Integer getStatusId(){
        return statusId;
    }


}
