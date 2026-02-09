INSERT INTO "user" (username, password, first_name, last_name, phone, email, is_admin) VALUES
('admin', 'admin', 'Djordje', 'Cekerevac', '1234567890', 'admin@gmail.com', TRUE),
('john_doe', 'password', 'John', 'Doe', '0987654321', 'john@gmail.com', FALSE),
('jane_smith', 'securepass', 'Jane', 'Smith', '5551234567', 'jane@gmail.com', FALSE),
('mike_ross', 'mikepass', 'Mike', 'Ross', '1112223333', 'mike@gmail.com', FALSE),
('rachel_zane', 'rachelpass', 'Rachel', 'Zane', '4445556666', 'rachel@gmail.com', FALSE);

INSERT INTO bike (type, price, status, location) VALUES
('Mountain Bike', 15.0, 1, 'Central Park'),
('Road Bike', 20.0, 0, 'Downtown'),
('Electric Bike', 25.0, 1, 'Beach Station'),
('Broken Bike', 5.0, -1, 'Workshop'),
('Hybrid Bike', 18.0, 1, 'Uptown'),
('Kids Bike', 10.0, 1, 'Park Entrance');

INSERT INTO rental (bike_id, user_id, start_time, end_time, total_price, photo_url) VALUES
(1, 2, '2023-10-27T10:00:00', '2023-10-27T12:00:00', 30.0, 'https://example.com/photo1.jpg'),
(3, 3, '2023-10-28T14:00:00', '2023-10-28T16:00:00', 50.0, 'https://example.com/photo2.jpg'),
(2, 4, '2023-10-29T09:00:00', '2023-10-29T11:00:00', 40.0, 'https://example.com/photo3.jpg'),
(5, 5, '2023-10-30T15:00:00', '2023-10-30T18:00:00', 54.0, 'https://example.com/photo4.jpg'),
(1, 3, '2023-11-01T10:00:00', '2023-11-01T14:00:00', 60.0, 'https://example.com/photo5.jpg'),
(6, 2, '2023-11-02T12:00:00', '2023-11-02T13:00:00', 10.0, 'https://example.com/photo6.jpg');

INSERT INTO report (bike_id, description, photo_url) VALUES
(4, 'Flat tire', 'https://example.com/report1.jpg'),
(2, 'Broken chain', 'https://example.com/report2.jpg'),
(1, 'Scratched frame', 'https://example.com/report3.jpg');
