package com.tm.config;

import com.tm.persistence.Level;
import com.tm.persistence.Seat;
import com.tm.repository.LevelRepository;
import com.tm.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.apache.log4j.Logger;

/**
 * Created by svallaban1 on 11/6/2015.
 */
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private Logger log = Logger.getLogger(DataLoader.class);

    private TicketRepository ticketRepository;

    private LevelRepository levelRepository;

    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Autowired
    public void setLevelRepository(LevelRepository levelRepository){
        this.levelRepository = levelRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Level level = new Level();
        level.setLevelId(1);
        level.setLevelStatus("1");
        level.setNoOfSeats(25);
        Seat seat = new Seat();
        seat.setSeatNo(1);
        seat.setSeatStatus(1);
        seat.setUserId("sample@gmail.com");

        seat.setLevel(level);
        levelRepository.save(level);
        ticketRepository.save(seat);

    }


}
