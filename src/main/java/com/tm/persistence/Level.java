package com.tm.persistence;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created by svallaban1 on 11/6/2015.
 */
@Entity
@Data
public class Level {

    @Id
    private Integer levelId;

    private Integer noOfSeats;

    private String levelStatus;

}
