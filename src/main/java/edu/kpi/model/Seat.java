package edu.kpi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seats",
    uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "number"}))
public class Seat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "session_id", nullable = false)
  private MovieSession session;

  @Column(nullable = false)
  private int number;

  @Column(name = "is_booked", nullable = false)
  private boolean isBooked;

  public Seat() {
  }

  public Seat(MovieSession session, int number) {
    this.session = session;
    this.number = number;
    this.isBooked = false;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MovieSession getSession() {
    return session;
  }

  public void setSession(MovieSession session) {
    this.session = session;
  }

  public long getSessionId() {
    return session != null ? session.getId() : 0L;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public boolean isBooked() {
    return isBooked;
  }

  public void setBooked(boolean booked) {
    isBooked = booked;
  }
}
