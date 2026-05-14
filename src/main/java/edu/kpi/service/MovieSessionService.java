package edu.kpi.service;

import edu.kpi.model.MovieSession;
import edu.kpi.model.Seat;
import edu.kpi.repository.dao.MovieSessionDao;
import edu.kpi.repository.dao.SeatDao;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class MovieSessionService {

  @Inject
  private MovieSessionDao sessionDao;

  @Inject
  private SeatDao seatDao;

  public List<MovieSession> getAllSessions() {
    return sessionDao.findAllSessions();
  }

  public List<MovieSession> getFilteredSessions(String title, int page, int size) {
    return sessionDao.findSessionsPaged(title, page, size);
  }

  public MovieSession getSession(long id) {
    return sessionDao.findSessionById(id);
  }

  public void addSession(MovieSession session) {
    MovieSession saved = sessionDao.saveSession(session);
    seatDao.createSeatsForSession(saved.getId(), 10);
  }

  public void updateSession(long id, MovieSession updated) {
    updated.setId(id);
    sessionDao.updateSession(updated);
  }

  public void deleteSession(long id) {
    sessionDao.deleteSession(id);
  }

  public long countSessions(String title) {
    return sessionDao.countSessions(title);
  }

  public List<Seat> getSeats(long sessionId) {
    return seatDao.findSeatsBySessionId(sessionId);
  }

  public boolean bookSeat(long sessionId, int seatNumber) {
    return seatDao.bookSeat(sessionId, seatNumber);
  }
}
