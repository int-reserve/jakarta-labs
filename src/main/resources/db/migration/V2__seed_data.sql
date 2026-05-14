-- Seed 100 movie sessions spread over 20 days (5 sessions per day).
-- Times are always on the hour or half-hour to satisfy the application validator.
-- Seats (10 per session) are inserted immediately after.

INSERT INTO movie_sessions (movie_title, start_time, price)
SELECT
    CASE (n - 1) % 20
        WHEN 0  THEN 'The Dark Knight'
        WHEN 1  THEN 'Inception'
        WHEN 2  THEN 'Interstellar'
        WHEN 3  THEN 'The Matrix'
        WHEN 4  THEN 'Avengers: Endgame'
        WHEN 5  THEN 'Dune: Part Two'
        WHEN 6  THEN 'Oppenheimer'
        WHEN 7  THEN 'Spider-Man: No Way Home'
        WHEN 8  THEN 'The Batman'
        WHEN 9  THEN 'Doctor Strange in the Multiverse of Madness'
        WHEN 10 THEN 'Thor: Love and Thunder'
        WHEN 11 THEN 'Black Panther: Wakanda Forever'
        WHEN 12 THEN 'Guardians of the Galaxy Vol. 3'
        WHEN 13 THEN 'Ant-Man and the Wasp: Quantumania'
        WHEN 14 THEN 'Indiana Jones and the Dial of Destiny'
        WHEN 15 THEN 'Mission: Impossible - Dead Reckoning'
        WHEN 16 THEN 'Fast X'
        WHEN 17 THEN 'The Flash'
        WHEN 18 THEN 'Barbie'
        WHEN 19 THEN 'Transformers: Rise of the Beasts'
    END AS movie_title,
    CURRENT_DATE
        + (((n - 1) / 5) + 1) * INTERVAL '1 day'
        + CASE (n - 1) % 5
              WHEN 0 THEN INTERVAL '10 hours'
              WHEN 1 THEN INTERVAL '13 hours 30 minutes'
              WHEN 2 THEN INTERVAL '16 hours'
              WHEN 3 THEN INTERVAL '18 hours 30 minutes'
              WHEN 4 THEN INTERVAL '21 hours'
          END                 AS start_time,
    CASE (n - 1) % 20
        WHEN 0  THEN 120.00
        WHEN 1  THEN 130.00
        WHEN 2  THEN 140.00
        WHEN 3  THEN 120.00
        WHEN 4  THEN 150.00
        WHEN 5  THEN 160.00
        WHEN 6  THEN 155.00
        WHEN 7  THEN 140.00
        WHEN 8  THEN 130.00
        WHEN 9  THEN 145.00
        WHEN 10 THEN 135.00
        WHEN 11 THEN 145.00
        WHEN 12 THEN 150.00
        WHEN 13 THEN 130.00
        WHEN 14 THEN 125.00
        WHEN 15 THEN 160.00
        WHEN 16 THEN 120.00
        WHEN 17 THEN 140.00
        WHEN 18 THEN 130.00
        WHEN 19 THEN 125.00
    END                       AS price
FROM generate_series(1, 100) AS n;

-- Create 10 seats for every session that was just inserted.
INSERT INTO seats (session_id, number, is_booked)
SELECT s.id, gs.seat_num, false
FROM movie_sessions s
CROSS JOIN generate_series(1, 10) AS gs(seat_num);
