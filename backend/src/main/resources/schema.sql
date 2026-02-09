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