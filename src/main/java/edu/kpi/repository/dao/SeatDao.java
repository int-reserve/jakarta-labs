package edu.kpi.repository.dao;

import edu.kpi.model.Seat;

import java.util.List;

public interface SeatDao {

  Seat findSeatById(long id);

  List<Seat> findSeatsBySessionId(long sessionId);

  Seat saveSeat(Seat seat);

  Seat updateSeat(Seat seat);

  void deleteSeat(long id);

  boolean bookSeat(long sessionId, int seatNumber);

  void createSeatsForSession(long sessionId, int count);
}
