ALTER TABLE app_user ADD COLUMN IF NOT EXISTS lobby_id BIGINT;

ALTER TABLE app_user
    ADD CONSTRAINT fk_lobby_sauvage
        FOREIGN KEY (lobby_id)
            REFERENCES lobby_sauvage (id)
            ON DELETE SET NULL;
