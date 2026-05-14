package edu.kpi.repository.dao.impl;

import edu.kpi.model.MovieSession;
import edu.kpi.model.Seat;
import edu.kpi.repository.JpaUtil;
import edu.kpi.repository.dao.SeatDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

@ApplicationScoped
public class SeatDaoImpl implements SeatDao {

  @Inject
  private JpaUtil jpaUtil;

  @Override
  public Seat findSeatById(long id) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      return em.find(Seat.class, id);
    }
  }

  @Override
  public List<Seat> findSeatsBySessionId(long sessionId) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      return em.createQuery(
          "SELECT s FROM Seat s WHERE s.session.id = :sessionId ORDER BY s.number",
          Seat.class)
          .setParameter("sessionId", sessionId)
          .getResultList();
    }
  }

  @Override
  public Seat saveSeat(Seat seat) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        em.persist(seat);
        tx.commit();
        return seat;
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw new RuntimeException("Error saving seat", e);
      }
    }
  }

  @Override
  public Seat updateSeat(Seat seat) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        Seat merged = em.merge(seat);
        tx.commit();
        return merged;
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw new RuntimeException("Error updating seat id=" + seat.getId(), e);
      }
    }
  }

  @Override
  public void deleteSeat(long id) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        Seat seat = em.find(Seat.class, id);
        if (seat != null) {
          em.remove(seat);
        }
        tx.commit();
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw new RuntimeException("Error deleting seat id=" + id, e);
      }
    }
  }

  @Override
  public boolean bookSeat(long sessionId, int seatNumber) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        int updated = em.createQuery(
            "UPDATE Seat s SET s.isBooked = true " +
            "WHERE s.session.id = :sessionId AND s.number = :number AND s.isBooked = false")
            .setParameter("sessionId", sessionId)
            .setParameter("number", seatNumber)
            .executeUpdate();
        tx.commit();
        return updated > 0;
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw new RuntimeException(
            "Error booking seat " + seatNumber + " for session_id=" + sessionId, e);
      }
    }
  }

  @Override
  public void createSeatsForSession(long sessionId, int count) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        MovieSession session = em.getReference(MovieSession.class, sessionId);
        for (int i = 1; i <= count; i++) {
          em.persist(new Seat(session, i));
        }
        tx.commit();
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw new RuntimeException("Error creating seats for session_id=" + sessionId, e);
      }
    }
  }
}
