package edu.kpi.repository.dao.impl;

import edu.kpi.model.MovieSession;
import edu.kpi.repository.JpaUtil;
import edu.kpi.repository.dao.MovieSessionDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class MovieSessionDaoImpl implements MovieSessionDao {

  @Inject
  private JpaUtil jpaUtil;

  @Override
  public MovieSession findSessionById(long id) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      return em.find(MovieSession.class, id);
    }
  }

  @Override
  public List<MovieSession> findAllSessions() {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      return em.createQuery(
          "SELECT s FROM MovieSession s ORDER BY s.startTime", MovieSession.class)
          .getResultList();
    }
  }

  @Override
  public List<MovieSession> findSessionsByTitle(String title) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      return em.createQuery(
          "SELECT s FROM MovieSession s WHERE LOWER(s.movieTitle) LIKE LOWER(:title) ORDER BY s.startTime",
          MovieSession.class)
          .setParameter("title", "%" + title + "%")
          .getResultList();
    }
  }

  @Override
  public List<MovieSession> findSessionsPaged(String title, int page, int size) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      boolean hasTitle = title != null && !title.isBlank();
      String jpql = hasTitle
          ? "SELECT s FROM MovieSession s WHERE LOWER(s.movieTitle) LIKE LOWER(:title) ORDER BY s.startTime"
          : "SELECT s FROM MovieSession s ORDER BY s.startTime";
      TypedQuery<MovieSession> q = em.createQuery(jpql, MovieSession.class);
      if (hasTitle) {
        q.setParameter("title", "%" + title + "%");
      }
      q.setFirstResult((page - 1) * size);
      q.setMaxResults(size);
      return q.getResultList();
    }
  }

  @Override
  public long countSessions(String title) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      boolean hasTitle = title != null && !title.isBlank();
      String jpql = hasTitle
          ? "SELECT COUNT(s) FROM MovieSession s WHERE LOWER(s.movieTitle) LIKE LOWER(:title)"
          : "SELECT COUNT(s) FROM MovieSession s";
      TypedQuery<Long> q = em.createQuery(jpql, Long.class);
      if (hasTitle) {
        q.setParameter("title", "%" + title + "%");
      }
      return q.getSingleResult();
    }
  }

  @Override
  public MovieSession saveSession(MovieSession session) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        em.persist(session);
        tx.commit();
        return session;
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw new RuntimeException("Error saving session", e);
      }
    }
  }

  @Override
  public MovieSession updateSession(MovieSession session) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        MovieSession merged = em.merge(session);
        tx.commit();
        return merged;
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw new RuntimeException("Error updating session id=" + session.getId(), e);
      }
    }
  }

  @Override
  public void deleteSession(long id) {
    try (EntityManager em = jpaUtil.createEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        MovieSession session = em.find(MovieSession.class, id);
        if (session != null) {
          em.remove(session);
        }
        tx.commit();
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw new RuntimeException("Error deleting session id=" + id, e);
      }
    }
  }
}
