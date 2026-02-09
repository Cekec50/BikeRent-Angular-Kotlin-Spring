INSERT INTO "user" (username, password, first_name, last_name, phone, email, is_admin) VALUES
('admin', 'admin', 'Djordje', 'Cekerevac', '1234567890', 'admin@gmail.com', TRUE),
('john_doe', 'password', 'John', 'Doe', '0987654321', 'john@gmail.com', FALSE),
('jane_smith', 'securepass', 'Jane', 'Smith', '5551234567', 'jane@gmail.com', FALSE);

INSERT INTO bike (type, price, status, location) VALUES
('Mountain Bike', 15.0, 1, 'Central Park'),
('Road Bike', 20.0, 0, 'Downtown'),
('Electric Bike', 25.0, 1, 'Beach Station'),
('Broken Bike', 5.0, -1, 'Workshop');