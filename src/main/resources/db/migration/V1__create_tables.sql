CREATE TABLE IF NOT EXISTS movie_sessions (
    id          BIGSERIAL      PRIMARY KEY,
    movie_title VARCHAR(100)   NOT NULL,
    start_time  TIMESTAMP      NOT NULL,
    price       NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS seats (
    id         BIGSERIAL PRIMARY KEY,
    session_id BIGINT    NOT NULL REFERENCES movie_sessions(id) ON DELETE CASCADE,
    number     INTEGER   NOT NULL,
    is_booked  BOOLEAN   NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_session_seat UNIQUE (session_id, number)
);
