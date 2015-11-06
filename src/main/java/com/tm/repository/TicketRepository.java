package com.tm.repository;

import com.tm.persistence.Seat;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by svallaban1 on 11/6/2015.
 */
public interface TicketRepository extends CrudRepository<Seat, Integer> {
}
