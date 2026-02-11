CREATE TABLE refresh_token
(
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_refresh_token FOREIGN KEY (user_id) REFERENCES app_user(id)
);

ALTER TABLE app_user
ADD COLUMN refresh_token_id SERIAL UNIQUE;