CREATE TABLE scrim_proposal (
    id SERIAL PRIMARY KEY,
    proposer_team_id INTEGER,
    proposer_scrim_team_id INTEGER,
    scrim_id INTEGER,
    status VARCHAR(255),
    CONSTRAINT fk_proposer_team FOREIGN KEY (proposer_team_id) REFERENCES team(id),
    CONSTRAINT fk_proposer_scrim_team FOREIGN KEY (proposer_scrim_team_id) REFERENCES scrim_team(id),
    CONSTRAINT fk_scrim FOREIGN KEY (scrim_id) REFERENCES scrim(id)
);