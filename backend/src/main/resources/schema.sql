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
    location VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS rental (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bike_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    total_price DOUBLE NOT NULL,
    photo_url VARCHAR(255),
    FOREIGN KEY (bike_id) REFERENCES bike(id),
    FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE IF NOT EXISTS report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bike_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    photo_url VARCHAR(255),
    FOREIGN KEY (bike_id) REFERENCES bike(id)
);
