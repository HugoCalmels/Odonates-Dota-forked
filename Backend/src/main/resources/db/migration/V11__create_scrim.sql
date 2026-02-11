CREATE TABLE scrim (
    id SERIAL PRIMARY KEY,
    "game_mode" VARCHAR(20),
    "game_type" VARCHAR(20),
    "game_status" VARCHAR(20),
    "pre_game_status" VARCHAR(20),
    start_date_time TIMESTAMP,
    average_rank_tier INTEGER,
    min_mmr_accepted INTEGER,
    max_mmr_accepted INTEGER,
    lobby_name VARCHAR(50),
    first_team_has_finished BOOLEAN DEFAULT false,
    second_team_has_finished BOOLEAN DEFAULT false,
    lobby_password VARCHAR(255)
);