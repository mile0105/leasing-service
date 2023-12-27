CREATE TABLE users(
    id              SERIAL  NOT NULL PRIMARY KEY,
    first_name      TEXT    NOT NULL,
    last_name       TEXT    NOT NULL,
    email           TEXT    NOT NULL,
    password        TEXT    NOT NULL
);

CREATE TABLE bikes(
    id              SERIAL  NOT NULL PRIMARY KEY,
    make            TEXT    NOT NULL,
    model           TEXT    NOT NULL,
    bike_owner_id   INT     NOT NULL REFERENCES users(id)
);

CREATE TABLE reservations(
    id                      SERIAL      NOT NULL PRIMARY KEY,
    bike_id                 INT         NOT NULL REFERENCES bikes(id),
    reservation_owner_id    INT         NOT NULL REFERENCES users(id),
    valid_from              TIMESTAMP   NOT NULL,
    valid_to                TIMESTAMP   NOT NULL
);
