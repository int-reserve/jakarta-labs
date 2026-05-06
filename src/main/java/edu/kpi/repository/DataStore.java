package edu.kpi.repository;

import edu.kpi.model.MovieSession;
import edu.kpi.model.Seat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataStore {
    private static final Map<String, MovieSession> sessions = new HashMap<>();
    private static final Map<String, List<Seat>> sessionSeats = new HashMap<>();

    static {
        // Initial mock data
        String id1 = UUID.randomUUID().toString();
        sessions.put(id1, new MovieSession(id1, "Dune: Part Two", LocalDateTime.now().plusDays(1), 150.0));
        sessionSeats.put(id1, createSeats(10));

        String id2 = UUID.randomUUID().toString();
        sessions.put(id2, new MovieSession(id2, "Oppenheimer", LocalDateTime.now().plusDays(2), 200.0));
        sessionSeats.put(id2, createSeats(10));
    }

    private static List<Seat> createSeats(int count) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            seats.add(new Seat(i, false));
        }
        return seats;
    }

    public static List<MovieSession> getAllSessions() {
        return new ArrayList<>(sessions.values());
    }

    public static MovieSession getSession(String id) {
        return sessions.get(id);
    }

    public static void addSession(MovieSession session) {
        sessions.put(session.getId(), session);
        sessionSeats.put(session.getId(), createSeats(10));
    }

    public static void deleteSession(String id) {
        sessions.remove(id);
        sessionSeats.remove(id);
    }

    public static List<Seat> getSeats(String sessionId) {
        return sessionSeats.get(sessionId);
    }

    public static void bookSeat(String sessionId, int seatNumber) {
        List<Seat> seats = sessionSeats.get(sessionId);
        if (seats != null) {
            for (Seat seat : seats) {
                if (seat.getNumber() == seatNumber) {
                    seat.setBooked(true);
                    break;
                }
            }
        }
    }
}
