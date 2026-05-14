package edu.kpi.repository.dao;

import edu.kpi.model.MovieSession;

import java.util.List;

public interface MovieSessionDao {

  MovieSession findSessionById(long id);

  List<MovieSession> findAllSessions();

  List<MovieSession> findSessionsByTitle(String title);

  List<MovieSession> findSessionsPaged(String title, int page, int size);

  long countSessions(String title);

  MovieSession saveSession(MovieSession session);

  MovieSession updateSession(MovieSession session);

  void deleteSession(long id);
}
