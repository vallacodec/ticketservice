package com.tm.repository;

import com.tm.model.Seat;
import com.tm.model.SeatHold;
import com.tm.model.SeatStatus;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by indsara on 11/7/15.
 */
@Repository
public class TicketServiceRepository {

    private SessionFactory sessionFactory;

    private static String QUERY_WITH_LEVEL_ID = "from Seat where levelId = :levelId and status = 2";

    private static String QUERY_WITHOUT_LEVEL_ID = "from Seat where status = 2";

    private static String UPDATE_HOLD_ID = "update Seat set seatStatus = :seatStatus, seatHoldId =:seatHoldId  where seatNo = :seatNo";

    private static String UPDATE_SEAT_DATA = "update Seat set seatStatus = :seatStatus where seatHoldId =:seatHoldId";

    private static String DELETE_SEAT_HOLD_DATA = "Delete SeatHold where seatHoldId =:seatHoldId";


    /**
     * TicketServiceRepository
     *
     * @param sessionFactory SessionFactory
     */
    @Autowired
    public TicketServiceRepository(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public Integer insertSeatHoldData(SeatHold seatHold) {
        com.tm.persistence.SeatHold seatHoldPersistence = new com.tm.persistence.SeatHold();
        seatHoldPersistence.setCustomerEmailId(seatHold.getCustomerEmailId());
        seatHoldPersistence.setSeatHoldTime(new Date());
        Session session = sessionFactory.getCurrentSession();
        session.save(seatHoldPersistence);
        return seatHoldPersistence.getSeatHoldId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public void updateSeatDetailsForHold(Seat seat, Integer seatHoldId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(UPDATE_HOLD_ID);
        query.setInteger("seatNo", seat.getSeatNo());
        query.setInteger("seatHoldId", seatHoldId);
        query.setInteger("seatStatus", SeatStatus.HOLD.getStatusId());
        query.executeUpdate();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public void updateSeatForAvailable(Integer seatHoldId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(UPDATE_SEAT_DATA);
        query.setInteger("seatHoldId", seatHoldId);
        query.setInteger("seatStatus", SeatStatus.AVAILABLE.getStatusId());
        query.executeUpdate();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public void deleteSeatHold(Integer seatHoldId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(DELETE_SEAT_HOLD_DATA);
        query.setInteger("seatHoldId", seatHoldId);
        query.executeUpdate();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public List<com.tm.persistence.Seat> getSeatDetails(Optional<Integer> levelId) {
        Session session = sessionFactory.openSession();
        Query query = null;
        if (levelId.isPresent()) {
            query = session.createQuery(QUERY_WITH_LEVEL_ID);
        } else {
            query = session.createQuery(QUERY_WITHOUT_LEVEL_ID);
        }
        query.setParameter("levelId", levelId.get());
        List<com.tm.persistence.Seat> results = query.list();
        return results;
    }

}
