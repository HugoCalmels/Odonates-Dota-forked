CREATE TYPE lobby_status AS ENUM ('WAITING_FOR_PLAYER', 'READY', 'CLOSED', 'LAUNCHED');

CREATE TABLE lobby_sauvage
(
    id               BIGSERIAL PRIMARY KEY,
    status           lobby_status NOT NULL,
    create_date_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    lobby_name       VARCHAR(355) NOT NULL
);

CREATE TABLE lobby_users
(
    lobby_id BIGINT NOT NULL,
    user_id  UUID NOT NULL,
    PRIMARY KEY (lobby_id, user_id),
    FOREIGN KEY (lobby_id) REFERENCES lobby_sauvage (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);
