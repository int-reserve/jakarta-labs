package edu.kpi.model;

import edu.kpi.validation.ValidSessionTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.json.bind.annotation.JsonbDateFormat;

import java.time.LocalDateTime;

public class MovieSession {
    private String id;

    @NotBlank(message = "Movie title cannot be empty")
    @Size(min = 2, max = 100, message = "Movie title must be between {min} and {max} characters")
    private String movieTitle;

    @NotNull(message = "Start time is required")
    @ValidSessionTime
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be strictly positive")
    private double price;

    public MovieSession() {
        // Default constructor required for JSON deserialization
    }

    public MovieSession(String id, String movieTitle, LocalDateTime startTime, double price) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
