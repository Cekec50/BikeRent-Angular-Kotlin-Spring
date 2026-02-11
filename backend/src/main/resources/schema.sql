CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(255),
    is_admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS bike (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255),
    price DOUBLE,
    status INT,
    location VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE
);

CREATE TABLE IF NOT EXISTS report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bike_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    photo_url VARCHAR(255),
    FOREIGN KEY (bike_id) REFERENCES bike(id)
);

CREATE TABLE IF NOT EXISTS history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    bike_id BIGINT NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    total_price DOUBLE,
    duration BIGINT,
    photo_url VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (bike_id) REFERENCES bike(id)
);
