package com.tm.repository;

import com.tm.model.Seat;
import com.tm.model.SeatHold;
import com.tm.model.SeatStatus;
import com.tm.service.impl.TicketServiceImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by indsara on 11/7/15.
 */
@Repository
public class TicketServiceRepository {

    private SessionFactory sessionFactory;

    private static String QUERY_WITH_LEVEL_ID = "from Seat where levelId = :levelId and seatStatus = 2";

    private static String QUERY_WITHOUT_LEVEL_ID = "from Seat where seatStatus = 2";

    private static String UPDATE_HOLD_ID = "update Seat set seatStatus = :seatStatus, seatHoldId =:seatHoldId  where seatNo = :seatNo";

    private static String UPDATE_SEAT_DATA = "update Seat set seatStatus = :seatStatus where seatHoldId =:seatHoldId";

    private static String DELETE_SEAT_HOLD_DATA = "Delete SeatHold where seatHoldId =:seatHoldId";

    private static String GET_SEAT_HOLD_TIME = "from SeatHold where seatHoldId =:seatHoldId";

    private static String EXPIRED_SEAT_HOLD = "select distinct(seatHoldId) from SeatHold where seatHoldTime >:expireHoldTime";

    @Autowired
    public TicketServiceRepository(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * method to insert the seat hold data into the SEAT_HOLD table
     *
     * @param seatHold
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public Integer insertSeatHoldData(SeatHold seatHold) {
        com.tm.persistence.SeatHold seatHoldPersistence = new com.tm.persistence.SeatHold();
        seatHoldPersistence.setCustomerEmailId(seatHold.getCustomerEmailId());
        seatHoldPersistence.setSeatHoldTime(new Date());
        Session session = sessionFactory.openSession();
        session.save(seatHoldPersistence);
        int seatHoldId = seatHoldPersistence.getSeatHoldId();
        session.close();
        return seatHoldId;
    }

    /**
     * method to update the seat hold details in the SEAT table
     *
     * @param seat
     * @param seatHoldId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public void updateSeatDetailsForHold(Seat seat, Integer seatHoldId) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(UPDATE_HOLD_ID);
        query.setInteger("seatNo", seat.getSeatNo());
        query.setInteger("seatHoldId", seatHoldId);
        query.setInteger("seatStatus", SeatStatus.HOLD.getStatusId());
        query.executeUpdate();
        session.close();
    }

    /**
     * method to update the seat status to RESERVED or AVAILABLE in the seat table
     *
     * @param seatHoldId
     * @param action
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public Integer updateSeatForAvailableOrReserved(Integer seatHoldId, SeatStatus action) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(UPDATE_SEAT_DATA);
        query.setInteger("seatHoldId", seatHoldId);
        if (action.equals(SeatStatus.AVAILABLE)) {
            query.setInteger("seatStatus", SeatStatus.AVAILABLE.getStatusId());
        } else if (action.equals(SeatStatus.RESERVED)) {
            query.setInteger("seatStatus", SeatStatus.AVAILABLE.getStatusId());
        }
        int updated = query.executeUpdate();
        session.close();
        return updated;
    }

    /**
     * delete the seat hold record in the seat_hold table
     *
     * @param seatHoldId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public int deleteSeatHold(Integer seatHoldId) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(DELETE_SEAT_HOLD_DATA);
        query.setInteger("seatHoldId", seatHoldId);
        int deleteCount = query.executeUpdate();
        session.close();
        return deleteCount;
    }

    /**
     * To fetch the seat details based on the level id
     *
     * @param levelId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public List<com.tm.persistence.Seat> getSeatDetails(Optional<Integer> levelId) {
        Session session = sessionFactory.openSession();
        Query query = null;
        if (levelId.isPresent()) {
            query = session.createQuery(QUERY_WITH_LEVEL_ID);
            query.setParameter("levelId", levelId.get());
        } else {
            query = session.createQuery(QUERY_WITHOUT_LEVEL_ID);
        }

        List<com.tm.persistence.Seat> results = query.list();
        List<Seat> seats = new ArrayList<>();
        session.close();
        return results;
    }

    /**
     * To get seat hold time from the seat_hold table
     *
     * @param seatHoldId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public Date getSeatHoldTime(int seatHoldId) {
        Date seatHoldTime = null;
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(GET_SEAT_HOLD_TIME);
        List<com.tm.persistence.SeatHold> results = query.list();
        return results.get(0).getSeatHoldTime();
    }

    /**
     * This method returns the expired hold in the seat_hold table
     * @return List Exprired Hold
     */
    public List<String> findExpiredHold() {
        List<String> expiredHolds = null;
        Calendar expireHoldTime = Calendar.getInstance();
        expireHoldTime.add(Calendar.MINUTE, -TicketServiceImpl.HOLD_TIME);
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(EXPIRED_SEAT_HOLD);
        query.setParameter("expireHoldTime", expireHoldTime.getTime());
        expiredHolds = query.list();
        return expiredHolds;
    }

}
