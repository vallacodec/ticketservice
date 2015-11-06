package com.tm.repository;

import com.tm.config.RepositoryConfiguration;
import com.tm.persistence.Level;
import com.tm.persistence.Seat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by svallaban1 on 11/6/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class})
public class TicketRepositoryTest {

    public Logger log = LoggerFactory.getLogger(TicketRepositoryTest.class);

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

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSaveLevel() {
        Level level = new Level();
        level.setLevelId(1);
        level.setLevelStatus("1");
        level.setNoOfSeats(25);
        Seat seat = new Seat();
        seat.setSeatNo(2);
        seat.setSeatStatus(1);
        seat.setUserId("sample@gmail.com");

        seat.setLevel(level);

        levelRepository.save(level);
        ticketRepository.save(seat);

        Level level1 = levelRepository.findOne(1);

        log.info("dsffdsfds "+level1.getNoOfSeats());












    }

}