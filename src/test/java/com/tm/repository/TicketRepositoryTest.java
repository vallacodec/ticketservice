package com.tm.repository;

import com.tm.config.DataBaseConfig;
import com.tm.config.RepositoryConfiguration;
import com.tm.persistence.Level;
import com.tm.persistence.Seat;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by svallaban1 on 11/6/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class,
        DataBaseConfig.class})
public class TicketRepositoryTest {

    public Logger log = LoggerFactory.getLogger(TicketRepositoryTest.class);

    private TicketRepository ticketRepository;

    private LevelRepository levelRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Autowired
    public void setLevelRepository(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Before
    public void setUp() throws Exception {
        Level level = new Level();
        level.setLevelId(1);
        level.setNoOfSeats(25);
        Seat seat = new Seat();
        seat.setSeatNo(2);
        seat.setSeatStatus(1);
        seat.setLevelId(1);
        levelRepository.save(level);
        ticketRepository.save(seat);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSaveLevel() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Seat where levelId = :levelId");
        query.setParameter("levelId", 1);
        List<Seat> results = query.list();
        results.forEach(s -> Assert.assertNotNull(s));
        Level level = levelRepository.findOne(1);

    }

}
