package com.tm.persistence;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by svallaban1 on 11/6/2015.
 */
@Entity
@Data
public class Seat {

    @Id
    public Integer seatNo;

    public Integer seatStatus;

    public String userId;

    @ManyToOne
    @JoinColumn(name="levelId")
    public Level level;


}
