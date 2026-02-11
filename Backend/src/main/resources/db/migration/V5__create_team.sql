CREATE TABLE team (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    logo VARCHAR(255),
    logo_name VARCHAR(255),
    captain_id UUID,
    CONSTRAINT fk_captain FOREIGN KEY (captain_id) REFERENCES app_user (id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE app_user
ADD COLUMN team_id BIGINT NULL;


