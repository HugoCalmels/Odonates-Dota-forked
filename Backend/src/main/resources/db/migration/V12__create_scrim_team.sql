
CREATE TABLE scrim_team (
    id SERIAL PRIMARY KEY,
    team_id BIGINT REFERENCES team(id),
    squad_captain_id UUID REFERENCES app_user(id)
);
ALTER TABLE scrim
    ADD COLUMN first_scrim_team_id BIGINT REFERENCES scrim_team(id),
    ADD COLUMN second_scrim_team_id BIGINT REFERENCES scrim_team(id);

CREATE TABLE scrim_team_users (
    scrim_team_id INT REFERENCES scrim_team(id),
    app_user_id UUID REFERENCES app_user(id),
    PRIMARY KEY (scrim_team_id, app_user_id)
);

