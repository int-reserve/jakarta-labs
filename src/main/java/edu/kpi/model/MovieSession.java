package edu.kpi.model;

import edu.kpi.validation.ValidSessionTime;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie_sessions")
public class MovieSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Movie title cannot be empty")
  @Size(min = 2, max = 100, message = "Movie title must be between {min} and {max} characters")
  @Column(name = "movie_title", nullable = false, length = 100)
  private String movieTitle;

  @NotNull(message = "Start time is required")
  @ValidSessionTime
  @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @DecimalMin(value = "0.0", inclusive = false, message = "Price must be strictly positive")
  @Column(nullable = false)
  private double price;

  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonbTransient
  private List<Seat> seats = new ArrayList<>();

  public MovieSession() {
  }

  public MovieSession(Long id, String movieTitle, LocalDateTime startTime, double price) {
    this.id = id;
    this.movieTitle = movieTitle;
    this.startTime = startTime;
    this.price = price;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMovieTitle() {
    return movieTitle;
  }

  public void setMovieTitle(String movieTitle) {
    this.movieTitle = movieTitle;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public List<Seat> getSeats() {
    return seats;
  }
}
