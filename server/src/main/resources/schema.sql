DROP TABLE IF EXISTS users, item_requests, items, bookings, comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL,
    available BOOLEAN NOT NULL,
    request BIGINT,
    CONSTRAINT fk_owner_id FOREIGN KEY (owner) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS item_requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description VARCHAR(500) NOT NULL,
    requester BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_requester_id FOREIGN KEY (requester) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS replies (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    request_id BIGINT NOT NULL,
    owner BIGINT NOT NULL,
    CONSTRAINT fk_owner_id2 FOREIGN KEY (owner) REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item BIGINT NOT NULL,
    booker BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_item_id FOREIGN KEY (item) REFERENCES items (id),
    CONSTRAINT fk_booker_id FOREIGN KEY (booker) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    text VARCHAR(200) NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT fk_item_id3 FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);