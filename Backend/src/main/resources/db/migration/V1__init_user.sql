CREATE TABLE app_user
(
    id        UUID NOT NULL,
    user_name VARCHAR(50),
    password  VARCHAR(50),
    email     VARCHAR(120),
    CONSTRAINT pk_user PRIMARY KEY (id)
);
